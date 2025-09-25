package id.bangkumis.tradewise.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio_assets")
data class PortfolioAssetEntity(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val amount: Double,
    val averagePrice: Double,
    val imageUrl: String,
)
