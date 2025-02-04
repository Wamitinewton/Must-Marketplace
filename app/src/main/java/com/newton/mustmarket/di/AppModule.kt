package com.newton.mustmarket.di

import android.app.Application
import androidx.room.Room
import com.newton.mustmarket.core.file_config.FileProcessor
import com.newton.mustmarket.core.file_config.ImageProcessingConfig
import com.newton.mustmarket.database.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DATASTORE_NAME = "must_market_preferences"

    @Provides
    @Singleton
    fun provideProductDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "products_db.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase) = db.productDao

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao

    @Provides
    @Singleton
    fun provideBookmarkDao(db: AppDatabase) = db.bookmarkDao

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.userDao

    @Provides
    @Singleton
    fun provideChatDao(database: AppDatabase) = database.chatDao

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