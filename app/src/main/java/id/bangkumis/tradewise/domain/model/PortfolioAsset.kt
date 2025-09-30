package id.bangkumis.tradewise.domain.model

import androidx.compose.ui.graphics.Color
import id.bangkumis.tradewise.data.db.entity.PortfolioAssetEntity

data class PortfolioAsset(
    val asset: PortfolioAssetEntity,
    val currentPrice: Double,
    val chartColor: Color = Color.Gray
) {
    val totalValue: Double = asset.amount * currentPrice
    val totalCost: Double = asset.amount * asset.averagePrice
    val profitLoss: Double = totalValue - totalCost
    val profitLossPercentage: Double = if (totalCost>0) (profitLoss / totalCost) * 100 else 0.0
}
