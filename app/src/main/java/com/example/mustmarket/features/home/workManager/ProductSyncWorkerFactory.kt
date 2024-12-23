package com.example.mustmarket.features.home.workManager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProductSyncWorkerFactoryEntryPoint {
    fun allProductRepository(): AllProductsRepository
    fun productSyncManager(): ProductSyncManager
}


class ProductSyncWorkerFactory @Inject constructor() : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerFactoryEntryPoint =
            EntryPoints.get(appContext, ProductSyncWorkerFactoryEntryPoint::class.java)

        return when (workerClassName) {
            ProductSyncWorker::class.java.name -> ProductSyncWorker(
                appContext,
                workerParameters,
                workerFactoryEntryPoint.allProductRepository(),
                workerFactoryEntryPoint.productSyncManager()
            )

            else -> null
        }
    }
}