package id.bangkumis.tradewise.domain.model

data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val currentPriceUSD: Double,
    val priceChangePercentage24h: Double
)
