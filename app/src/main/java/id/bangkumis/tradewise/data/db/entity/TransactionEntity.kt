package id.bangkumis.tradewise.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val assetId: String,
    val type: String,
    val amount: Double,
    val price: Double,
    val timestamp: Long
)
