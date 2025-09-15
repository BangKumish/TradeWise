package id.bangkumis.tradewise.domain.usecase

import id.bangkumis.tradewise.data.db.dao.PortfolioDao
import id.bangkumis.tradewise.data.db.entity.PortfolioAssetEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
   private val portfolioDao: PortfolioDao
){
    operator fun invoke(): Flow<List<PortfolioAssetEntity>> {
        return portfolioDao.getAllAssets()
    }
}