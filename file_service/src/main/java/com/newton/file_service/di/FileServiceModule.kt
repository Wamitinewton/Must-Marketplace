package com.newton.file_service.di

import com.newton.mustmarket.core.file_config.FileProcessor
import com.newton.mustmarket.core.file_config.ImageProcessingConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FileServiceModule {

    @Provides
    fun provideFileProcessor(): FileProcessor {
        return FileProcessor(
            ImageProcessingConfig(
                maxDimension = 2048,
                maxFileSize = 10 * 1024 * 1024,
                compressionQuality = 0.8f
            )
        )
    }
}