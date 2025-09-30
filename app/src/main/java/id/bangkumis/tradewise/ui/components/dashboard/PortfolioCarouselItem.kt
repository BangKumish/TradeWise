package id.bangkumis.tradewise.ui.components.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.bangkumis.tradewise.domain.model.PortfolioAsset
import id.bangkumis.tradewise.ui.theme.LossRed
import id.bangkumis.tradewise.ui.theme.ProfitGreen

@Composable
fun PortfolioCarouselItem(
    portfolioAsset: PortfolioAsset,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    val asset = portfolioAsset.asset
    val profitColor = if(portfolioAsset.profitLoss >= 0.0) ProfitGreen else LossRed
    val profitIcon = if(portfolioAsset.profitLoss >= 0.0) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

    Card(
        modifier = modifier
            .width(180.dp)
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                AsyncImage(
                    model = asset.imageUrl,
                    contentDescription = asset.name,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = asset.symbol.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = asset.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$${"%.2f".format(portfolioAsset.totalValue)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = profitIcon,
                        contentDescription = "Profit",
                        tint = profitColor
                    )
                    Text(
                        text = "${"%.2f".format(portfolioAsset.profitLossPercentage)}%",
                        color = profitColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}