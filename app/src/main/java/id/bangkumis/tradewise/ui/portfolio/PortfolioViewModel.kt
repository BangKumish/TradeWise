package id.bangkumis.tradewise.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.bangkumis.tradewise.data.db.entity.PortfolioAssetEntity
import id.bangkumis.tradewise.domain.usecase.GetPortfolioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class PortfolioState(
    val assets: List<PortfolioAssetEntity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase
): ViewModel() {
    private val _state = MutableStateFlow(PortfolioState())
    val state = _state.asStateFlow()

    init {
        getPortfolio()
    }

    private fun getPortfolio() {
        getPortfolioUseCase().onEach { assets ->
            _state.value = PortfolioState(assets = assets)
        }.launchIn(viewModelScope)
    }
}