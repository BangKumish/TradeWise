package id.bangkumis.tradewise.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.bangkumis.tradewise.data.db.dao.PortfolioDao
import id.bangkumis.tradewise.domain.model.CoinDetail
import id.bangkumis.tradewise.domain.repository.CoinRepository
import id.bangkumis.tradewise.domain.usecase.ExecuteTransactionUseCase
import id.bangkumis.tradewise.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val ownedAmount: Double = 0.0,
    val error: String = "",
    val isShowingDialog: Boolean = false,
    val transactionType: String = "BUY"
)

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val portfolioDao: PortfolioDao,
    private val executeTransactionUseCase: ExecuteTransactionUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(CoinDetailState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("coinID")?.let { coinID ->
            if(coinID.isNotBlank()){
                getCoinDetail(coinID)
                observeOwnedAmount(coinID)
            }
        }
    }

    private fun observeOwnedAmount(coinID: String) {
        portfolioDao.getAssetByIDasFlow(coinID).onEach { asset ->
            _state.value = _state.value.copy(
                ownedAmount = asset?.amount ?: 0.0
            )
        }.launchIn(viewModelScope)
    }

    private fun getCoinDetail(coinID: String){
        coinRepository.getCoinDetail(coinID).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        coin = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message ?: "An unexpected error occurred",
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onBuySellButtonClicked(type: String){
        _state.value = _state.value.copy(
            isShowingDialog = true,
            transactionType = type
        )
    }

    fun onDismissDialog(){
        _state.value = _state.value.copy(
            isShowingDialog = false
        )
    }

    fun onConfirmTransaction(quantity: String){
        viewModelScope.launch {
            val coin = _state.value.coin?: return@launch
            val transactionType = _state.value.transactionType
            val price = coin.currentPriceUSD

            executeTransactionUseCase(
                assetID = coin.id,
                symbol = coin.symbol,
                name = coin.name,
                transactionType = transactionType,
                quantityString = quantity,
                pricePerAsset = price
            )

            _eventFlow.emit("Transaction Successful")
        }
        onDismissDialog()
    }
}

