package id.bangkumis.tradewise.domain.repository

import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.data.model.MarketChartDto
import id.bangkumis.tradewise.domain.model.CoinDetail
import id.bangkumis.tradewise.util.Resource
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoinMarkets(): Flow<Resource<List<CoinMarketDto>>>
    fun getCoinDetail(id: String): Flow<Resource<CoinDetail>>
    suspend fun refreshCoinMarkets(): Boolean
    fun getCoinMarketChart(coinID: String, days: String): Flow<Resource<MarketChartDto>>
}