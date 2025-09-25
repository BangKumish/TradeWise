package id.bangkumis.tradewise.data

import id.bangkumis.tradewise.data.db.entity.CoinDetailEntity
import id.bangkumis.tradewise.data.db.entity.MarketCoinEntity
import id.bangkumis.tradewise.data.model.CoinDetailDto
import id.bangkumis.tradewise.data.model.CoinMarketDto
import id.bangkumis.tradewise.domain.model.CoinDetail

fun CoinMarketDto.toEntity(): MarketCoinEntity{
    return MarketCoinEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        currentPrice = this.currentPrice,
        image = this.image,
        priceChangePercentage24h = this.priceChangePercentage24h

    )
}

fun MarketCoinEntity.toDto(): CoinMarketDto {
    return CoinMarketDto(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        currentPrice = this.currentPrice,
        image = this.image,
        priceChangePercentage24h = this.priceChangePercentage24h!!

    )
}

fun CoinDetailDto.toEntity(): CoinDetailEntity {
    return CoinDetailEntity(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        description = this.description.en.ifBlank { "No description available." },
        imageUrl = this.image.large,
        currentPriceUSD = this.marketData.currentPrice.usd,
        priceChangePercentage24h = this.marketData.priceChangePercentage24h
    )
}

fun CoinDetailDto.toDomain(): CoinDetail {
    return CoinDetail(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        description = this.description.en.ifBlank { "No description available." },
        imageUrl = this.image.large,
        currentPriceUSD = this.marketData.currentPrice.usd,
        priceChangePercentage24h = this.marketData.priceChangePercentage24h
    )
}

fun CoinDetailEntity.toDomain(): CoinDetail{
    return CoinDetail(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        currentPriceUSD = this.currentPriceUSD,
        priceChangePercentage24h = this.priceChangePercentage24h
    )
}

//fun CoinDetailEntity.toDto(): CoinDetailDto {
//    val imageDto = ImageDto(this.imageUrl)
//    val descriptionDto = DescriptionDto(this.description)
//    val currencyDto = CurrencyDto(this.currentPriceUSD)
//
//    val marketDataDto = MarketDataDto(
//        currentPrice = currencyDto,
//        marketCap = currencyDto,
//        totalVolume = currencyDto,
//        high24h = currencyDto,
//        low24h = currencyDto,
//        priceChangePercentage24h = this.priceChangePercentage24h,
//        circulatingSupply = 0.0
//    )
//
//    return CoinDetailDto(
//        id = this.id,
//        symbol = this.symbol,
//        name = this.name,
//        image = imageDto,
//        description = descriptionDto,
//        marketCapRank = 0,
//        marketData = marketDataDto
//    )
//}