package id.bangkumis.tradewise.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.bangkumis.tradewise.data.db.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
}