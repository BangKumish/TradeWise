package id.bangkumis.tradewise.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market_coins")
data class MarketCoinEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val currentPrice: Double,
    val image: String,
    val priceChangePercentage24h: Double?
)