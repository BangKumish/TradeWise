package id.bangkumis.tradewise.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.domain.repository.CoinRepository
import id.bangkumis.tradewise.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class DashboardState(
    val isLoading: Boolean = false,
    val coins: List<CoinMarketDto> = emptyList(),
    val error: String = ""
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinRepository: CoinRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getCoinMarketsData()
    }

    private fun getCoinMarketsData(){
        coinRepository.getCoinMarkets().onEach { result ->
            when(result){
                is Resource.Success -> {
                    val coinList = result.data
                    Log.d("MainViewModel", "Status: Success! Data received: ${coinList?.size} coins")
                    Log.d("MainViewModel", "First coin: ${coinList?.firstOrNull()}")
                    _state.value = DashboardState(coins = result.data?: emptyList())
                }

                is Resource.Error -> {
                    val errorMessage = result.message
                    Log.d("MainViewModel", "Status: Error! Error message: $errorMessage")
                    _state.value = DashboardState(error = result.message?: "An Error Occurred")
                }

                is Resource.Loading -> {
                    Log.d("MainViewModel", "Status: Loading...")
                    _state.value = DashboardState(isLoading = true)
                }

            }
        }.launchIn(viewModelScope)
    }
}