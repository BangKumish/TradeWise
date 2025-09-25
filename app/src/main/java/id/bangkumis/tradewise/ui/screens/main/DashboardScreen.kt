package id.bangkumis.tradewise.ui.screens.main

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.ui.screens.Screen
import id.bangkumis.tradewise.ui.components.CoinListItem
import id.bangkumis.tradewise.ui.components.PortfolioListItem

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
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "OmCabul",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "$5,000",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Owned Assets",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to Portfolio",
                        modifier = Modifier.clickable { onNavigateToPortfolio() }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(state.portfolioAssets.take(2)) { asset ->
                PortfolioListItem(portfolioAsset = asset, onItemClick = { onItemClick(asset.asset.id) })
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .2f))
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Market",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(
                items = state.coins,
                key = { coin -> coin.id }
            ) { coin ->
                CoinListItem(coin = coin, onItemClick = onItemClick)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .2f))
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
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
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