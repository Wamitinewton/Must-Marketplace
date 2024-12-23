package com.example.mustmarket.application

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.mustmarket.features.auth.data.authWorkManager.TokenRefreshWorkerFactory
import com.example.mustmarket.features.auth.data.authWorkManager.scheduleTokenRefreshWork
import com.example.mustmarket.features.home.workManager.ProductSyncManager
import com.example.mustmarket.features.home.workManager.ProductSyncWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MustApplication : Application(), ImageLoaderFactory, Configuration.Provider {

    @Inject
    lateinit var workerFactory: TokenRefreshWorkerFactory

    @Inject
    lateinit var productSyncWorkerFactory: ProductSyncWorkerFactory

    @Inject
    lateinit var syncManager: ProductSyncManager


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        scheduleTokenRefreshWork(this)
        setupSyncManager()
    }

    override val workManagerConfiguration: Configuration by lazy(LazyThreadSafetyMode.PUBLICATION) {
        val compositeWorkerFactory = CompositeWorkerFactory(
            listOf(
                workerFactory,
                productSyncWorkerFactory
            )
        )

        Configuration.Builder()
            .setExecutor(Dispatchers.Default.asExecutor())
            .setWorkerFactory(compositeWorkerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }

    private fun setupSyncManager() {
        syncManager.startPeriodicSync()
        syncManager.observeSyncWork()
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .logger(DebugLogger())
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(.1)
                    .strongReferencesEnabled(true)

                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(.03)
                    .directory(cacheDir)
                    .build()
            }
            .build()
    }

}