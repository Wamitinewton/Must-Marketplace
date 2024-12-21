package com.example.mustmarket.features.auth.data.authWorkManager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TokenRefreshWorkerFactoryEntryPoint {
    fun authRepository(): AuthRepository
}

class TokenRefreshWorkerFactory @Inject constructor() : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerFactoryEntryPoint =
            EntryPoints.get(appContext, TokenRefreshWorkerFactoryEntryPoint::class.java)
        val authRepository = workerFactoryEntryPoint.authRepository()

        return when (workerClassName) {
            TokenRefreshWorker::class.java.name -> TokenRefreshWorker(
                appContext,
                workerParameters,
                authRepository
            )

            else -> null

        }
    }

}