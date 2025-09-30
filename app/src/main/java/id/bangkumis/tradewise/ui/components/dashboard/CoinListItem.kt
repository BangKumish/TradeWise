package id.bangkumis.tradewise.ui.components.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.domain.model.PortfolioAsset

@Composable
fun CoinListItem(
    coin: CoinMarketDto,
    onItemClick: (String) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .fillMaxWidth()
            .clickable { onItemClick(coin.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coin.image,
            contentDescription = coin.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = coin.name, fontWeight = FontWeight.Bold)
            Text(text = coin.symbol.uppercase(), style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End){
            Text(text = "$${"%.2f".format(coin.currentPrice)}", fontWeight = FontWeight.Bold)
            val priceChangeColor = if(coin.priceChangePercentage24h >= 0) Color(0xFF4CAF50) else Color.Red
            Text(
                text = "${"%.2f".format(coin.priceChangePercentage24h)}%",
                color = priceChangeColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PortfolioListItem(
    portfolioAsset: PortfolioAsset,
    onItemClick: (String) -> Unit
) {
    val asset = portfolioAsset.asset
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(asset.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = asset.imageUrl,
            contentDescription = asset.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = asset.name, fontWeight = FontWeight.Bold)
            Text(text = "${asset.amount} ${asset.symbol.uppercase()}", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End){
            Text(text = "$${"%.2f".format(portfolioAsset.totalValue)}", fontWeight = FontWeight.Bold)
            val profitColor = if (portfolioAsset.profitLoss >= 0) Color(0xFF4CAF50) else Color.Red
            Text(
                text = "${if (portfolioAsset.profitLoss >= 0) "+" else ""}${"%.2f".format(portfolioAsset.profitLoss)} (${"%.2f".format(portfolioAsset.profitLossPercentage)}%)",
                color = profitColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}