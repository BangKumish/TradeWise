package id.bangkumis.tradewise.data.remote

import id.bangkumis.tradewise.data.model.CoinDetailDto
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.data.model.MarketChartDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("coins/markets")
    suspend fun getCoinMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
    ): List<CoinMarketDto>

    @GET("coins/{id}")
    suspend fun getCoinDetail(
        @Path("id") coinId: String
    ): CoinDetailDto

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") coinID: String,
        @Query("days") days: String = "1",
        @Query("vs_currency") vsCurrency: String = "usd"
    ): MarketChartDto

    companion object {
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
    }
}