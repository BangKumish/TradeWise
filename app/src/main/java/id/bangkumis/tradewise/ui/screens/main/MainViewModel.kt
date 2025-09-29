package id.bangkumis.tradewise.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.bangkumis.tradewise.data.model.CoinMarketDto
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
import kotlin.collections.associateBy

data class DashboardState(
    val isLoading: Boolean = false,
    val coins: List<CoinMarketDto> = emptyList(),
    val portfolioAssets: List<PortfolioAsset> = emptyList(),
    val portfolioSummary: PortfolioSummary = PortfolioSummary(),
    val error: String = "",
    val topPortfolioAsset: List<PortfolioAsset> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val getPortfolioUseCase: GetPortfolioUseCase
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        observeDashboardData()
    }

    private fun observeDashboardData() {
        combine(
            coinRepository.getCoinMarkets(),
            getPortfolioUseCase()
        ) { marketResult, portfolioEntities ->
            val marketPriceMap = (marketResult.data?: emptyList()).associateBy { it.id }

            val portfolioAssets =  portfolioEntities.map { entity ->
                PortfolioAsset(
                    asset = entity,
                    currentPrice = marketPriceMap[entity.id]?.currentPrice ?: entity.averagePrice
                )
            }

            val totalValue = portfolioAssets.sumOf { it.totalValue }
            val totalCost = portfolioAssets.sumOf { it.totalCost }
            val totalProfitLoss = totalValue - totalCost
            val profitLossPercentage = if (totalCost > 0.0) (totalProfitLoss / totalCost) * 100 else 0.0

            val summary = PortfolioSummary(
                totalValue = totalValue,
                totalProfitLoss = totalProfitLoss,
                profitLossPercentage
            )

            val sortedAndLimitedPortfolio = portfolioAssets
                .sortedByDescending { it.totalValue }
                .take(10)

            when(marketResult){
                is Resource.Success -> {
                    _state.value = DashboardState(
                        isLoading = false,
                        coins = marketResult.data ?: emptyList(),
                        portfolioAssets = portfolioAssets,
                        portfolioSummary = summary,
                        topPortfolioAsset = sortedAndLimitedPortfolio
                    )
                }
                is Resource.Error -> {
                    _state.value = DashboardState(
                        isLoading = false,
                        error = marketResult.message ?: "An error occurred",
                        portfolioAssets = portfolioAssets
                    )
                }
                is Resource.Loading -> {
                    _state.value = DashboardState(
                        isLoading = true,
                        portfolioAssets = portfolioAssets
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}