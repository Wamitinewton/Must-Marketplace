package com.example.mustmarket.application

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MustApplication: Application(), ImageLoaderFactory{
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