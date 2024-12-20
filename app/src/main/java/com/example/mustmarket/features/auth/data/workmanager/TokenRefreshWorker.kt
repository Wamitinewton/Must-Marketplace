package com.example.mustmarket.features.auth.data.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.network.HttpException
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORK_NAME = "TokenRefreshWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            Timber.d("REFRESHINGGGGGG TOKEEEEENSSSS. jfhnjifnjfnifgfrtdyuhjifuyhdcjnifyuhdcjnk")
            val freshTokens = authRepository.refreshTokenFromServer()

            freshTokens?.let {
                authRepository.storeAuthTokens(
                    accessToken = it.data.token,
                    refreshToken = it.data.refreshToken
                )
            }
            Timber.d("Token refresh successful")
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}