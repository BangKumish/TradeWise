package id.bangkumis.tradewise.ui.components.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.bangkumis.tradewise.domain.model.PortfolioSummary
import id.bangkumis.tradewise.ui.theme.LossRed
import id.bangkumis.tradewise.ui.theme.ProfitGreen

@Composable
fun NetWorthCard(
    summary: PortfolioSummary,
    modifier: Modifier = Modifier
){
    var animatedNetWorth by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(summary.totalValue) {
        animatedNetWorth = summary.totalValue.toFloat()
    }
    val animatedValue by animateFloatAsState(
        targetValue = animatedNetWorth,
        animationSpec = tween(durationMillis = 1000),
        label = "Net Worth Animation"
    )
    val profitColor = if(summary.totalProfitLoss >= 0.0) ProfitGreen else LossRed

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            Modifier.padding(24.dp)
        ) {
            Text(
                text = "Total Net Worth",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$${"%.2f".format(animatedValue)}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "${if (summary.totalProfitLoss >= 0) "+" else ""}${"%.2f".format(summary.totalProfitLoss)} (${"%.2f".format(summary.totalProfitLossPercentage)}%) Total Profit",
                style = MaterialTheme.typography.titleMedium,
                color = profitColor
            )
        }

    }
}