package id.bangkumis.tradewise.ui.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.ui.Screen
import id.bangkumis.tradewise.ui.components.CoinListItem

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    Log.d("DashboardScreen", "Recomposing with State: isLoading=${state.isLoading}, error=${state.error}, coins=${state.coins.size}")

    DashboardContent(
        state = state,
        onItemClick = { coinID ->
            navController.navigate(Screen.CoinDetailScreen.withArgs(coinID))
        }
    )
}

@Composable
fun DashboardContent(
    state: DashboardState,
    onItemClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()){
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

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                state.coins,
                key = { coin -> coin.id }
                ) { coin ->
                CoinListItem(
                    coin = coin,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview_Success(){
    val fakeCoin = listOf(
        CoinMarketDto(id = "bitcoin", name = "Bitcoin", symbol = "btc", currentPrice = 69000.0, image = "", priceChangePercentage24h = 2.5),
        CoinMarketDto(id = "ethereum", name = "Ethereum", symbol = "eth", currentPrice = 3500.0, image = "", priceChangePercentage24h = -1.2)
    )

    DashboardContent(
        state = DashboardState( coins = fakeCoin),
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview_Loading(){
    DashboardContent(
        state = DashboardState(isLoading = true),
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview_Error(){
    DashboardContent(
        state = DashboardState(error = "Something went wrong"),
        onItemClick = {}
    )
}