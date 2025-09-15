package id.bangkumis.tradewise.ui.portfolio

import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import id.bangkumis.tradewise.ui.Screen
import id.bangkumis.tradewise.ui.components.CoinListItem

@Composable
fun PortfolioScreen(
    navController: NavController,
    viewModel: PortfolioViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState().value
    PortfolioContent(
        state = state,
        onItemClick = {assetID ->
            navController.navigate(Screen.CoinDetailScreen.withArgs(assetID))
        }
    )
}

@Composable
fun PortfolioContent(
    state: PortfolioState,
    onItemClick: (String) -> Unit
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Text(
                text = "My Portfolio",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        items(
            state.assets,
            key = { asset -> asset.id}
        ) { asset ->
            CoinListItem(asset = asset, onItemClick = onItemClick)
        }
    }
}