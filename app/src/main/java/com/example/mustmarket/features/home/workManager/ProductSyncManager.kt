package com.example.mustmarket.features.home.workManager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.asFlow
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductSyncManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val workManager: WorkManager,
    @ApplicationContext private val context: Context
) {
    private val lastSyncKey = longPreferencesKey("last_product_sync")
    private val syncStateFlow = MutableStateFlow(ProductSyncState())

    companion object {
        const val PRODUCT_SYNC_WORK_NAME = "product_sync_work"
        const val KEY_ERROR_MSG = "error_msg"
    }

    suspend fun updateLastSyncTimeStamp() {
        dataStore.edit { prefs ->
            prefs[lastSyncKey] = System.currentTimeMillis()
        }
    }

    fun startPeriodicSync() {
        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<ProductSyncWorker>(
            1, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constrains)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            PRODUCT_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    fun observeSyncWork() {
        workManager.getWorkInfosForUniqueWorkLiveData(PRODUCT_SYNC_WORK_NAME)
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