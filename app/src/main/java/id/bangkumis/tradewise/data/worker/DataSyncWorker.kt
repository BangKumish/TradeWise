package id.bangkumis.tradewise.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import id.bangkumis.tradewise.domain.repository.CoinRepository

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val coinRepository: CoinRepository
): CoroutineWorker(context, workerParams){
    override suspend fun doWork(): Result {
        return if (coinRepository.refreshCoinMarkets()){
            Result.success()
        } else {
            Result.failure()
        }
    }
}