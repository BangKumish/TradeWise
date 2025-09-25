package id.bangkumis.tradewise.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.bangkumis.tradewise.domain.model.PortfolioAsset
import id.bangkumis.tradewise.domain.model.PortfolioSummary
import id.bangkumis.tradewise.domain.repository.CoinRepository
import id.bangkumis.tradewise.domain.usecase.GetPortfolioUseCase
import id.bangkumis.tradewise.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

enum class SortType { BY_NAME, BY_VALUE, BY_AMOUNT }
enum class SortOrder { ASCENDING, DESCENDING }

data class PortfolioState(
    val assets: List<PortfolioAsset> = emptyList(),
    val isLoading: Boolean = false,
    val sortType: SortType = SortType.BY_VALUE,
    val sortOrder: SortOrder= SortOrder.DESCENDING,
    val isSortMenuVisible: Boolean = false,
    val summary: PortfolioSummary = PortfolioSummary()
)

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val coinRepository: CoinRepository
): ViewModel() {
    private val _state = MutableStateFlow(PortfolioState())
    val state = _state.asStateFlow()

    init {
        observePortfolio()
    }

    fun onSortChanged(sortType: SortType){
        _state.value = _state.value.copy(sortType = sortType, isSortMenuVisible = false)
    }

    fun onSortOrderChanged(sortOrder: SortOrder){
        _state.value = _state.value.copy(sortOrder = sortOrder)
    }

    fun onToggleSortMenu(isVisible: Boolean){
        _state.value = _state.value.copy(isSortMenuVisible = isVisible)
    }

    private fun observePortfolio() {
        combine(
            getPortfolioUseCase(),
            coinRepository.getCoinMarkets(),
            _state
        ) { portfolioEntities, marketResult, currentState ->

            val marketData = if (marketResult is Resource.Success) marketResult.data else emptyList()
            val marketPriceMap = marketData?.associateBy { it.id } ?: emptyMap()

            val portfolioAssets = portfolioEntities.map { entity ->
                PortfolioAsset(
                    asset = entity,
                    currentPrice = marketPriceMap[entity.id]?.currentPrice ?: entity.averagePrice
                )
            }

            val sortedAssets = when (currentState.sortType){
                SortType.BY_NAME -> if (currentState.sortOrder == SortOrder.ASCENDING) portfolioAssets.sortedBy { it.asset.name } else portfolioAssets.sortedByDescending { it.asset.name }
                SortType.BY_VALUE -> if (currentState.sortOrder == SortOrder.ASCENDING) portfolioAssets.sortedBy { it.totalValue } else portfolioAssets.sortedByDescending { it.totalValue }
                SortType.BY_AMOUNT -> if (currentState.sortOrder == SortOrder.ASCENDING) portfolioAssets.sortedBy { it.asset.amount } else portfolioAssets.sortedByDescending { it.asset.amount }
            }

            val totalValue = portfolioAssets.sumOf { it.totalValue }
            val totalCost = portfolioAssets.sumOf { it.totalCost }
            val totalProfitLoss = totalValue - totalCost
            val totalProfitLossPercentage = if (totalCost>0) (totalProfitLoss / totalCost) * 100 else 0.0

            val summary = PortfolioSummary(
                totalValue = totalValue,
                totalProfitLoss = totalProfitLoss,
                totalProfitLossPercentage = totalProfitLossPercentage
            )

            _state.value = currentState.copy(assets = sortedAssets, summary = summary)
        }.launchIn(viewModelScope)
    }
}