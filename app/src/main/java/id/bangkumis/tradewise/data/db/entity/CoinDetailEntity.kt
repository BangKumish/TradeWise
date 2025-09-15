package id.bangkumis.tradewise.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_details")
data class CoinDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val description: String,
    val imageUrl: String,
    val currentPriceUSD: Double,
    val priceChangePercentage24h: Double,
)
