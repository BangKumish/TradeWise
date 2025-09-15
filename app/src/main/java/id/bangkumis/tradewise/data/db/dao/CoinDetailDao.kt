package id.bangkumis.tradewise.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import id.bangkumis.tradewise.data.db.entity.CoinDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDetailDao {
    @Upsert
    suspend fun upsertCoinDetail(coinDetail: CoinDetailEntity)

    @Query("SELECT * FROM coin_details WHERE id = :coinID")
    fun getCoinDetail(coinID: String): Flow<CoinDetailEntity?>
}