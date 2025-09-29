package id.bangkumis.tradewise.ui.screens.main

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.ui.screens.Screen
import id.bangkumis.tradewise.ui.components.CoinListItem
import id.bangkumis.tradewise.ui.components.NetWorthCard
import id.bangkumis.tradewise.ui.components.PortfolioCarouselItem
import id.bangkumis.tradewise.ui.components.loading.ShimmerListItem

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
        },
        onNavigateToPortfolio = { navController.navigate(Screen.PortfolioScreen.route) }
    )
}

@Composable
fun DashboardContent(
    state: DashboardState,
    onItemClick: (String) -> Unit,
    onNavigateToPortfolio: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                NetWorthCard(
                    summary = state.portfolioSummary,
                    Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "My Assets",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to Portfolio",
                        modifier = Modifier.clickable { onNavigateToPortfolio() }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if(state.isLoading){
                        items(5){
                            ShimmerListItem(Modifier.width(150.dp))
                        }
                    } else {
                        items(state.topPortfolioAsset,
                            key = {it.asset.id }
                        ) { asset ->
                            PortfolioCarouselItem(
                                portfolioAsset = asset,
                                onItemClick = {onItemClick(asset.asset.id)}
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Market",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (state.isLoading && state.coins.isEmpty()) {
                items(7){
                    ShimmerListItem()
                }
            } else {
                items(
                    items = state.coins,
                    key = { coin -> coin.id }
                ) { coin ->
                    CoinListItem(coin = coin, onItemClick = onItemClick)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .2f))
                }
            }
        }
        if (state.error.isNotBlank()){
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
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
        onItemClick = {},
        onNavigateToPortfolio = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview_Loading(){
    DashboardContent(
        state = DashboardState(isLoading = true),
        onItemClick = {},
        onNavigateToPortfolio = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview_Error(){
    DashboardContent(
        state = DashboardState(error = "Something went wrong"),
        onItemClick = {},
        onNavigateToPortfolio = {}
    )
}