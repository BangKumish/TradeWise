package id.bangkumis.tradewise.domain.usecase

import id.bangkumis.tradewise.data.db.dao.PortfolioDao
import id.bangkumis.tradewise.data.db.dao.TransactionDao
import id.bangkumis.tradewise.data.db.entity.PortfolioAssetEntity
import id.bangkumis.tradewise.data.db.entity.TransactionEntity
import javax.inject.Inject

class ExecuteTransactionUseCase @Inject constructor(
    private val portfolioDao: PortfolioDao,
    private val transactionDao: TransactionDao
) {
    suspend operator fun invoke(
        assetID: String,
        symbol: String,
        name: String,
        transactionType: String,
        quantityString: String,
        pricePerAsset: Double,
        imageUrl: String,
    ) {
        val quantity = quantityString.toDoubleOrNull() ?: return
        val currentAsset = portfolioDao.getAssetByID(assetID)

        if(transactionType == "BUY"){
            val newQuantity = (currentAsset?.amount?: 0.0) + quantity
            val newTotalValue = (currentAsset?.amount?: 0.0) * (currentAsset?.averagePrice?: 0.0) + (quantity * pricePerAsset)
            val newAveragePrice = if (newQuantity > 0) newTotalValue / newQuantity else 0.0

            val updatedAsset = PortfolioAssetEntity(
                id = assetID,
                symbol = symbol,
                name = name,
                amount = newQuantity,
                averagePrice = newAveragePrice,
                imageUrl = imageUrl
            )
            portfolioDao.upsertAsset(updatedAsset)
        } else {
            val currentAmount = currentAsset?.amount?: 0.0

            if (quantity > currentAmount) {
                throw IllegalArgumentException("Insufficient coins shithead")
            }

            val newQuantity = currentAmount - quantity
            if (newQuantity <= 0.00000001) {
                portfolioDao.deleteAsset(assetID)
            } else {
                val updatedAsset = currentAsset!!.copy(amount = newQuantity)
                portfolioDao.upsertAsset(updatedAsset)
            }
        }

        val newTransaction = TransactionEntity(
            assetId = assetID,
            type = transactionType,
            amount = quantity,
            price = pricePerAsset,
            timestamp = System.currentTimeMillis()
        )
        transactionDao.insertTransaction(newTransaction)
    }
}