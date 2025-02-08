package com.newton.file_service.di

import com.newton.file_service.data.remote.api_service.ImageUploadApi
import com.newton.file_service.data.repository.ImageUploadRepositoryImpl
import com.newton.file_service.domain.repository.ImageUploadRepository
import com.newton.mustmarket.core.file_config.FileProcessor
import com.newton.mustmarket.core.file_config.ImageProcessingConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideImageUploadRepository(
        api: ImageUploadApi,
        fileProcessor: FileProcessor,
        @IODispatcher dispatcher: CoroutineDispatcher
    ): ImageUploadRepository = ImageUploadRepositoryImpl(api, fileProcessor, dispatcher)

    @Provides
    @Singleton
    fun provideImageServiceApi(retrofit: Retrofit): ImageUploadApi {
        return retrofit.create(ImageUploadApi::class.java)
    }
}
