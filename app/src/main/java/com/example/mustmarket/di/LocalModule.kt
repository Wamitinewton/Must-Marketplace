package com.example.mustmarket.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mustmarket.features.auth.data.datastore.UserStoreManager
import com.example.mustmarket.database.database.AppDatabase
import com.example.mustmarket.features.home.secureStorage.SecureProductStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.userDao

    @Provides
    @Singleton
    fun provideSecuredProductStorage(
        @ApplicationContext context: Context
    ): SecureProductStorage {
        return SecureProductStorage(context)
    }

    @Provides
    @Singleton
    fun provideUserStore(
        @ApplicationContext context: Context
    ): UserStoreManager {
        return UserStoreManager(context)

    }
}