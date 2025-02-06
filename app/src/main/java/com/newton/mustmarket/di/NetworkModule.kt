package com.newton.mustmarket.di

import android.content.Context
import com.newton.mustmarket.BuildConfig
import com.newton.mustmarket.features.auth.authInterceptor.AuthInterceptor
import com.newton.mustmarket.features.auth.data.remote.service.AuthenticationService
import com.newton.mustmarket.features.home.data.remote.api_service.ProductsApi
import com.newton.mustmarket.features.merchant.products.data.remote.UploadProductsApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.newton.mustmarket.features.merchant.store.data.remote.api_service.MerchantServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            .build()



    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val baseKey = BuildConfig.SERVER_BASE_URL
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return Retrofit.Builder()
            .baseUrl(baseKey)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthenticationService = retrofit.create(AuthenticationService::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit): ProductsApi =
        retrofit.create(ProductsApi::class.java)

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): UploadProductsApi {
        return retrofit.create(UploadProductsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMerchantApi(retrofit: Retrofit): MerchantServices {
        return retrofit.create(MerchantServices::class.java)
    }

}