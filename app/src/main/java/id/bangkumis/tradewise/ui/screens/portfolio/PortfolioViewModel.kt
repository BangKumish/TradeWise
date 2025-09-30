package id.bangkumis.tradewise.ui.screens.portfolio

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.bangkumis.tradewise.data.db.entity.PortfolioAssetEntity
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.domain.model.PortfolioAsset
import id.bangkumis.tradewise.domain.model.PortfolioSummary
import id.bangkumis.tradewise.domain.repository.CoinRepository
import id.bangkumis.tradewise.domain.usecase.GetDominantColorUseCase
import id.bangkumis.tradewise.domain.usecase.GetPortfolioUseCase
import id.bangkumis.tradewise.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    private val coinRepository: CoinRepository,
    private val getDominantColorUseCase: GetDominantColorUseCase
): ViewModel() {
    private val _state = MutableStateFlow(PortfolioState())
    val state = _state.asStateFlow()

    init {
        observePortfolio()
        observeAndFetchColors()
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

    private fun observePortfolio(){
        combine(
            getPortfolioUseCase(),
            coinRepository.getCoinMarkets(),
            _state
        ) { portfolioEntities, marketResult, currentState ->
            val portfolioAsset = mapPortfolioAsset(portfolioEntities, marketResult )
            val sortedAssets = sortPortfolioAsset(portfolioAsset, currentState.sortType, currentState.sortOrder)
            val summary = calculatePortfolioSummary(sortedAssets)

            currentState.copy(assets = sortedAssets, summary = summary)
        } .onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }

    private fun mapPortfolioAsset(
      entities: List<PortfolioAssetEntity>,
      marketResult: Resource<List<CoinMarketDto>>
    ): List<PortfolioAsset>{
        val marketData = (marketResult as? Resource.Success)?.data ?: emptyList()
        val marketPriceMap = marketData.associateBy { it.id }

        return entities.map { entity ->
            PortfolioAsset(
                asset = entity,
                currentPrice = marketPriceMap[entity.id]?.currentPrice?: entity.averagePrice
            )
        }
    }

    private fun sortPortfolioAsset(
        assets: List<PortfolioAsset>,
        sortType: SortType,
        sortOrder: SortOrder
    ): List<PortfolioAsset> {
        return when (sortType) {
            SortType.BY_NAME -> if (sortOrder == SortOrder.ASCENDING) assets.sortedBy { it.asset.name } else assets.sortedByDescending { it.asset.name }
            SortType.BY_VALUE -> if (sortOrder == SortOrder.ASCENDING) assets.sortedBy { it.totalValue } else assets.sortedByDescending { it.totalValue }
            SortType.BY_AMOUNT -> if (sortOrder == SortOrder.ASCENDING) assets.sortedBy { it.asset.amount } else assets.sortedByDescending { it.asset.amount }
        }
    }

    private fun calculatePortfolioSummary(asset: List<PortfolioAsset>): PortfolioSummary {
        val totalValue = asset.sumOf { it.totalValue }
        val totalCost = asset.sumOf { it.totalCost }
        val totalProfitLoss = totalValue - totalCost
        val totalProfitLossPercentage = if (totalCost>0) (totalProfitLoss / totalCost) * 100 else 0.0

        return PortfolioSummary(
            totalValue = totalValue,
            totalProfitLoss = totalProfitLoss,
            totalProfitLossPercentage = totalProfitLossPercentage
        )
    }

    private fun observeAndFetchColors(){
        viewModelScope.launch {
            state.map { it.assets }
                .distinctUntilChanged()
                .collect { assets ->
                    val assetToUpdate = assets.filter { it.chartColor == Color.Gray && it.asset.imageUrl.isNotBlank()}
                    if(assetToUpdate.isNotEmpty()){
                        viewModelScope.launch {
                            val updatedAsset = _state.value.assets.map { currentAsset ->
                                val assetToUpdate = assetToUpdate.find { it.asset.id == currentAsset.asset.id }
                                if (assetToUpdate != null) {
                                    val color = getDominantColorUseCase(assetToUpdate.asset.imageUrl)
                                    currentAsset.copy(chartColor = color)
                                } else {
                                    currentAsset
                                }
                            }
                            _state.value = _state.value.copy(assets = updatedAsset)
                        }
                    }
                }
        }
    }
}