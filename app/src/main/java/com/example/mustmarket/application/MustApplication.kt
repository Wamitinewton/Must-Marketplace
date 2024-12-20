package com.example.mustmarket.application

import android.app.Application
<<<<<<< HEAD
import androidx.work.Configuration
=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
<<<<<<< HEAD
import com.example.mustmarket.features.auth.data.workmanager.TokenRefreshWorkerFactory
import com.example.mustmarket.features.auth.data.workmanager.scheduleTokenRefreshWork
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MustApplication : Application(), ImageLoaderFactory, Configuration.Provider {

    @Inject
    lateinit var workerFactory: TokenRefreshWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        scheduleTokenRefreshWork(this)
    }

    override val workManagerConfiguration: Configuration by lazy {
        Configuration.Builder()
            .setExecutor(Dispatchers.Default.asExecutor())
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }


=======
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MustApplication: Application(), ImageLoaderFactory{
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
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