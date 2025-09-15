package id.bangkumis.tradewise.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import id.bangkumis.tradewise.data.db.entity.MarketCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketCoinDao {
    @Upsert
    suspend fun upsertAll(coins: List<MarketCoinEntity>)

    @Query("SELECT * FROM market_coins")
    fun getMarketCoins(): Flow<List<MarketCoinEntity>>

    @Query("DELETE FROM market_coins")
    suspend fun clearAll()
}