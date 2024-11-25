package com.example.mustmarket.di

import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.products.data.remote.UploadProductsApi
import com.example.mustmarket.features.products.data.repository.ProductRepositoryImpl
import com.example.mustmarket.features.products.domain.repository.ProductRepository
import com.example.mustmarket.features.products.domain.usecases.AddProductUseCase
import com.example.mustmarket.features.products.domain.usecases.ProductUseCases
import com.example.mustmarket.features.products.domain.usecases.UploadImageListUseCase
import com.example.mustmarket.features.products.domain.usecases.UploadSingleImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): UploadProductsApi {
        return retrofit.create(UploadProductsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesProductUseCases(repository: ProductRepository): ProductUseCases {
        return ProductUseCases(
            uploadImage = UploadSingleImageUseCase(repository),
            uploadImageList = UploadImageListUseCase(repository),
            addProduct = AddProductUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providesProductRepository(
        api: UploadProductsApi,
        dispatcher: CoroutineDispatcher
    ): ProductRepository {
        return ProductRepositoryImpl(api, dispatcher = dispatcher)
    }
}