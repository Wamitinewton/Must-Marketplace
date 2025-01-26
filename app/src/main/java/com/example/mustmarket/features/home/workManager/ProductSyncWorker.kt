package com.example.mustmarket.features.home.workManager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber

@HiltWorker
class ProductSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: AllProductsRepository,
    private val syncManager: ProductSyncManager
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            Timber.d("Syncing products")
            val result = repository.getAllProducts(forceRefresh = true)
                .first { it !is Resource.Loading }

            when (result) {
                is Resource.Error -> {
                    val outputData = workDataOf(
                        ProductSyncManager.KEY_ERROR_MSG to result.message
                    )
                    Result.failure(outputData)
                }

                is Resource.Success -> {
                    syncManager.updateLastSyncTimeStamp()
                    Result.success()
                }

                else -> Result.retry()
            }
        } catch (e: Exception) {
            val outputData = workDataOf(
                ProductSyncManager.KEY_ERROR_MSG to (e.message ?: "Unknown error occurres")
            )
            Result.failure(outputData)
        }
    }
}