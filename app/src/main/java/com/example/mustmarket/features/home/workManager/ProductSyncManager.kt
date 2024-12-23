package com.example.mustmarket.features.home.workManager

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.mustmarket.features.auth.data.datastore.DatastoreKeys.PREF_FILE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val syncStateFlow = MutableStateFlow(ProductSyncState())

    fun getLastSyncState(): StateFlow<ProductSyncState> = syncStateFlow.asStateFlow()

    companion object {
        const val PRODUCT_SYNC_WORK_NAME = "product_sync_work"
        const val KEY_ERROR_MSG = "error_msg"
        const val LAST_SYNC_TIME_KEY = "last_sync_time"
    }

    private val workManager = WorkManager.getInstance(context)

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePreferences = EncryptedSharedPreferences.create(
        context,
        PREF_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    fun updateLastSyncTimeStamp() {
        securePreferences.edit().apply {
            putLong(LAST_SYNC_TIME_KEY, System.currentTimeMillis())
            apply()
        }
    }

    fun startPeriodicSync() {
        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<ProductSyncWorker>(
            4, TimeUnit.SECONDS,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constrains)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                PRODUCT_SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
    }

    fun observeSyncWork() {
        workManager
            .getWorkInfosForUniqueWorkLiveData(PRODUCT_SYNC_WORK_NAME)
            .asFlow()
            .onEach { workInfoList ->
                workInfoList.firstOrNull()?.let { workInfo ->
                    syncStateFlow.update { currentState ->
                        currentState.copy(
                            isSyncing = workInfo.state == WorkInfo.State.RUNNING,
                            syncError = workInfo.outputData.getString(KEY_ERROR_MSG)
                        )
                    }
                }
            }
            .launchIn(CoroutineScope(Dispatchers.Main + SupervisorJob()))
    }
}