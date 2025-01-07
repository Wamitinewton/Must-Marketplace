package com.example.mustmarket.di

import com.example.mustmarket.core.threadExecutor.ThreadPoolExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThreadingModule {

    @Provides
    @Singleton
    fun provideExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            // Handle exceptions here
            throwable.printStackTrace()
        }
    }


    @Provides
    @Singleton
    fun provideMultiThreadingActivity(
        @IODispatcher ioDispatcher: CoroutineDispatcher,
        exceptionHandler: CoroutineExceptionHandler
    ): ThreadPoolExecutor {
        return ThreadPoolExecutor(ioDispatcher, exceptionHandler)
    }
}