package id.bangkumis.tradewise.ui.screens.portfolio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import id.bangkumis.tradewise.ui.screens.Screen
import id.bangkumis.tradewise.ui.components.PortfolioListItem

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
        },
        onSortChange = viewModel::onSortChanged,
        onSortOrderChanged = viewModel::onSortOrderChanged,
        onToggleSortMenu = viewModel::onToggleSortMenu
    )
}

@Composable
fun PortfolioContent(
    state: PortfolioState,
    onItemClick: (String) -> Unit,
    onSortChange: (SortType) -> Unit,
    onSortOrderChanged: (SortOrder) -> Unit,
    onToggleSortMenu: (Boolean) -> Unit
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Total Portfolio Value", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "$${"%.2f". format(state.summary.totalValue)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    val profitColor = if (state.summary.totalProfitLoss >= 0) Color(0xFF4CAF50) else Color.Red
                    Text(
                        text = "${if (state.summary.totalProfitLoss >= 0) "+" else ""}${"%.2f".format(state.summary.totalProfitLoss)} (${"%.2f".format(state.summary.totalProfitLossPercentage)}%) Today",
                        style = MaterialTheme.typography.bodyLarge,
                        color = profitColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "My Portfolio",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                SortMenu(
                    state = state,
                    onSortChanged = onSortChange,
                    onSortOrderChanged = onSortOrderChanged,
                    onToggleSortMenu = onToggleSortMenu
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(
            state.assets,
            key = { asset -> asset.asset.id}
        ) { asset ->
            PortfolioListItem (portfolioAsset = asset, onItemClick = onItemClick)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(.2f))
        }
    }
}

@Composable
fun SortMenu(
    state: PortfolioState,
    onSortChanged: (SortType) -> Unit,
    onSortOrderChanged: (SortOrder) -> Unit,
    onToggleSortMenu: (Boolean) -> Unit
){
    Box{
        Row(
            modifier = Modifier.clickable { onToggleSortMenu(true) },
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Sort By",
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open Sort Menu"
            )
        }
        DropdownMenu(
            expanded = state.isSortMenuVisible,
            onDismissRequest = { onToggleSortMenu(false) }
        ) {
            DropdownMenuItem(
                text = { Text("By Value") },
                onClick = { onSortChanged(SortType.BY_VALUE) },
                trailingIcon = {
                    if (state.sortType == SortType.BY_VALUE) {
                        Icon(Icons.Default.Check, "Selected")
                    }
                }
            )
            DropdownMenuItem(
                text = { Text("By Name") },
                onClick = { onSortChanged(SortType.BY_NAME) },
                trailingIcon = {
                    if (state.sortType == SortType.BY_NAME) {
                        Icon(Icons.Default.Check, "Selected")
                    }
                }
            )
            DropdownMenuItem(
                text = { Text("By Amount") },
                onClick = { onSortChanged(SortType.BY_AMOUNT) },
                trailingIcon = {
                    if (state.sortType == SortType.BY_AMOUNT) {
                        Icon(Icons.Default.Check, "Selected")
                    }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Order") },
                trailingContent = {
                    Row {
                        IconButton(onClick = { onSortOrderChanged(SortOrder.ASCENDING) }) {
                            Icon(Icons.Default.KeyboardArrowUp, "Ascending", tint = if(state.sortOrder == SortOrder.ASCENDING) MaterialTheme.colorScheme.primary else Color.Gray)
                        }
                        IconButton(onClick = { onSortOrderChanged(SortOrder.DESCENDING) }) {
                            Icon(Icons.Default.KeyboardArrowDown, "Descending", tint = if(state.sortOrder == SortOrder.DESCENDING) MaterialTheme.colorScheme.primary else Color.Gray)
                        }
                    }
                }
            )
        }
    }
}