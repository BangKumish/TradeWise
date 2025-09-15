package id.bangkumis.tradewise.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.bangkumis.tradewise.data.db.entity.PortfolioAssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAsset(asset: PortfolioAssetEntity)

    @Query("SELECT * FROM portfolio_assets")
    fun getAllAssets(): Flow<List<PortfolioAssetEntity>>

    @Query("SELECT * FROM portfolio_assets WHERE id = :assetId")
    fun getAssetByIDasFlow(assetId: String): Flow<PortfolioAssetEntity?>

    @Query("SELECT * FROM portfolio_assets WHERE id = :assetId")
    suspend fun getAssetByID(assetId: String): PortfolioAssetEntity?

    @Query("DELETE FROM portfolio_assets WHERE id = :assetId")
    suspend fun deleteAsset(assetId: String)
}