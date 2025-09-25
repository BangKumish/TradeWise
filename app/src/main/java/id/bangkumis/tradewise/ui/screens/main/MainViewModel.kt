package id.bangkumis.tradewise.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.domain.model.PortfolioAsset
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
    val error: String = ""
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

            when(marketResult){
                is Resource.Success -> {
                    _state.value = DashboardState(
                        isLoading = false,
                        coins = marketResult.data ?: emptyList(),
                        portfolioAssets = portfolioAssets
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