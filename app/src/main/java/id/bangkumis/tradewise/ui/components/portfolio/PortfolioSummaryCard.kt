package id.bangkumis.tradewise.ui.components.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.bangkumis.tradewise.domain.model.PortfolioAsset
import id.bangkumis.tradewise.domain.model.PortfolioSummary
import id.bangkumis.tradewise.ui.theme.LossRed
import id.bangkumis.tradewise.ui.theme.ProfitGreen
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun PortfolioSummaryCard(
    summary: PortfolioSummary,
    portfolioAsset: List<PortfolioAsset>,
    modifier: Modifier = Modifier
) {
    val pieChartData = PieChartData(
        slices = portfolioAsset.map { asset ->
            PieChartData.Slice(
                value = asset.totalValue.toFloat(),
                color = asset.chartColor
            )
        }
    )

    val profitColor = if(summary.totalProfitLoss >= 0) ProfitGreen else LossRed

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Portfolio Value",
                style = MaterialTheme.typography.titleMedium,
                color =MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${"%.2f".format(summary.totalValue)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${if (summary.totalProfitLoss >= 0) "+" else ""}${"%.2f".format(summary.totalProfitLoss)} (${"%.2f".format(summary.totalProfitLossPercentage)}%) Total P/L",
                style = MaterialTheme.typography.bodyLarge,
                color = profitColor
                )
            Spacer(modifier = Modifier.height(16.dp))

            if (portfolioAsset.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PieChart(
                        pieChartData = pieChartData,
                        modifier = Modifier.size(150.dp),
                        animation = simpleChartAnimation(),
                        sliceDrawer = SimpleSliceDrawer(35f)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        portfolioAsset.forEachIndexed { index, asset ->
                            val slice = pieChartData.slices[index]
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(slice.color)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = asset.asset.symbol.uppercase(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
            else {
                Text(
                    text = "No Asset in Portfolio",
                    modifier = Modifier.padding(vertical = 48.dp)
                )
            }
        }
    }
}