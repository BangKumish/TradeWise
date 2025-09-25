package id.bangkumis.tradewise.domain.model

data class PortfolioSummary(
    val totalValue: Double = 0.0,
    val totalProfitLoss: Double = 0.0,
    val totalProfitLossPercentage: Double = 0.0
)