package id.bangkumis.tradewise.domain.repository

import id.bangkumis.tradewise.data.db.dao.CoinDetailDao
import id.bangkumis.tradewise.data.db.dao.MarketCoinDao
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.data.model.MarketChartDto
import id.bangkumis.tradewise.data.remote.ApiService
import id.bangkumis.tradewise.data.toDomain
import id.bangkumis.tradewise.data.toDto
import id.bangkumis.tradewise.data.toEntity
import id.bangkumis.tradewise.domain.model.CoinDetail
import id.bangkumis.tradewise.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import okio.IOException
import javax.inject.Inject


class CoinRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val marketCoinDao: MarketCoinDao,
    private val coinDetailDao: CoinDetailDao
): CoinRepository{

    override fun getCoinMarkets(): Flow<Resource<List<CoinMarketDto>>> = flow {
        emit(Resource.Loading())

        marketCoinDao.getMarketCoins().collect { entities ->
            emit(Resource.Success(entities.map { it.toDto() }))
        }

        try {
            val freshCoins = apiService.getCoinMarkets()
            marketCoinDao.clearAll()
            marketCoinDao.upsertAll(freshCoins.map { it.toEntity() })
        } catch (e: HttpException) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error(
                "Couldn't reach server. Check your internet connection." +
                    "Detail: ${e.localizedMessage}"
                )
            )
        }

        val updatedCoins = marketCoinDao.getMarketCoins().first().map { it.toDto() }
        emit(Resource.Success(updatedCoins))
    }

    override fun getCoinDetail(id: String): Flow<Resource<CoinDetail>> = flow {
        emit(Resource.Loading())

        val cachedDetail = coinDetailDao.getCoinDetail(id).first()
        if (cachedDetail != null) {
            emit(Resource.Success(cachedDetail.toDomain()))
        }

        try {
            val freshDetailDao = apiService.getCoinDetail(id)
            coinDetailDao.upsertCoinDetail(freshDetailDao.toEntity())
            val newCachedData = coinDetailDao.getCoinDetail(id).first()
            if (newCachedData != null) {
                emit(Resource.Success(newCachedData.toDomain()))
            }
        } catch (e: HttpException) {
            if (cachedDetail == null) {
                emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
            }
        } catch (e: IOException) {
            if (cachedDetail == null) {
                emit(Resource.Error(
                    "Couldn't reach server. Check your internet connection." +
                            "Detail: ${e.localizedMessage}"
                    )
                )
            }
        }
    }

    override suspend fun refreshCoinMarkets(): Boolean {
        return try {
            val freshCoins = apiService.getCoinMarkets()
            marketCoinDao.clearAll()
            marketCoinDao.upsertAll(freshCoins.map { it.toEntity() })
            true
        } catch (e: HttpException) {
            false
        }
    }

    override fun getCoinMarketChart(
        coinID: String,
        days: String
    ): Flow<Resource<MarketChartDto>> = flow {
        emit(Resource.Loading())
        try {
            val chartData = apiService.getMarketChart(coinID, days)
            emit(Resource.Success(chartData))
        } catch (e: HttpException) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
        }
    }
}