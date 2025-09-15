package id.bangkumis.tradewise.data.model

import com.google.gson.annotations.SerializedName

data class  CoinDetailDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("image")
    val image: ImageDto,

    @SerializedName("description")
    val description: DescriptionDto,

    @SerializedName("market_cap_rank")
    val marketCapRank: Int,

    @SerializedName("market_data")
    val marketData: MarketDataDto
)

data class ImageDto(
    @SerializedName("large")
    val large: String,
)

data class DescriptionDto(
    @SerializedName("en")
    val en: String
)

data class MarketDataDto(
    @SerializedName("current_price")
    val currentPrice: CurrencyDto,

    @SerializedName("market_cap")
    val marketCap: CurrencyDto,

    @SerializedName("total_volume")
    val totalVolume: CurrencyDto,

    @SerializedName("high_24h")
    val high24h: CurrencyDto,

    @SerializedName("low_24h")
    val low24h: CurrencyDto,

    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,

    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
)

data class CurrencyDto(
    @SerializedName("usd")
    val usd: Double
)