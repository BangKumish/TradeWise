package id.bangkumis.tradewise.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collect{ event ->
            snackBarHostState.showSnackbar(message = event)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState ) }
    ) { innerPadding ->
        CoinDetailContent(
            state = state,
            modifier = Modifier.padding(innerPadding),
            onBuyClick = { viewModel.onBuySellButtonClicked("BUY") },
            onSellClick = { viewModel.onBuySellButtonClicked("SELL") },
            onConfirmTransaction = { viewModel.onConfirmTransaction(it) },
            onDismissDialog = { viewModel.onDismissDialog() }
        )
    }
}

@Composable
fun CoinDetailContent(
    state: CoinDetailState,
    modifier: Modifier = Modifier,
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit,
    onConfirmTransaction: (String) -> Unit,
    onDismissDialog: () -> Unit
    ){
    Box(
        modifier = modifier.fillMaxSize()
    ){
        if (state.isLoading){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        if (state.error.isNotBlank()){
            Text(
                text = state.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        state.coin?.let { coin ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "You own ${state.ownedAmount} ${coin.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Symbol: ${coin.symbol.uppercase()}",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Price: $${coin.currentPriceUSD}",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Button(onClick = onBuyClick) { Text("Buy")}
                    if (state.ownedAmount > .00000001){
                        Button(onClick = onSellClick) { Text("Sell")}
                    }
                }
            }
        }
        if (state.isShowingDialog && state.coin != null){
            TransactionDialog(
                onDismiss = onDismissDialog,
                onConfirm = onConfirmTransaction,
                assetName = state.coin.name,
                assetPrice = state.coin.currentPriceUSD,
                transactionType = state.transactionType,
                ownedAmount = state.ownedAmount
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinDetailContentPreview(){
    CoinDetailContent(
        state = CoinDetailState(error = "This is a Preview"),
        onBuyClick = {},
        onSellClick = {},
        onConfirmTransaction = {},
        onDismissDialog = {}
    )
}
