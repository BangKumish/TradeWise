package id.bangkumis.tradewise.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import id.bangkumis.tradewise.data.db.dao.CoinDetailDao
import id.bangkumis.tradewise.data.db.dao.MarketCoinDao
import id.bangkumis.tradewise.data.db.dao.PortfolioDao
import id.bangkumis.tradewise.data.db.dao.TransactionDao
import id.bangkumis.tradewise.data.db.entity.CoinDetailEntity
import id.bangkumis.tradewise.data.db.entity.MarketCoinEntity
import id.bangkumis.tradewise.data.db.entity.PortfolioAssetEntity
import id.bangkumis.tradewise.data.db.entity.TransactionEntity


@Database(
    entities = [
        PortfolioAssetEntity::class,
        TransactionEntity::class,
        MarketCoinEntity::class,
        CoinDetailEntity::class
               ],
    version = 4,
    exportSchema = false
)
abstract class TradeWiseDatabase: RoomDatabase() {

    abstract fun portfolioDao(): PortfolioDao
    abstract fun transactionDao(): TransactionDao
    abstract fun marketCoinDao(): MarketCoinDao
    abstract fun coinDetailDao(): CoinDetailDao
}