package com.example.mustmarket.di

import android.app.Application
import androidx.room.Room
import com.example.mustmarket.features.home.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
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
}