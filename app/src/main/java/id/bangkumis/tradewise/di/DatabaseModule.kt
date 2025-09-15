package id.bangkumis.tradewise.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.bangkumis.tradewise.data.db.TradeWiseDatabase
import id.bangkumis.tradewise.data.db.dao.CoinDetailDao
import id.bangkumis.tradewise.data.db.dao.MarketCoinDao
import id.bangkumis.tradewise.data.db.dao.PortfolioDao
import id.bangkumis.tradewise.data.db.dao.TransactionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TradeWiseDatabase{
        return Room.databaseBuilder(
            context,
            TradeWiseDatabase::class.java,
            "tradewise_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun providePortfolioDao(database: TradeWiseDatabase): PortfolioDao{
        return database.portfolioDao()
    }

    @Provides
    fun provideTransactionDao(database: TradeWiseDatabase): TransactionDao{
        return database.transactionDao()
    }

    @Provides
    fun provideMarketCoinDao(database: TradeWiseDatabase): MarketCoinDao {
        return database.marketCoinDao()
    }

    @Provides
    fun provideCoinDetailDao(database: TradeWiseDatabase): CoinDetailDao {
        return database.coinDetailDao()
    }
}