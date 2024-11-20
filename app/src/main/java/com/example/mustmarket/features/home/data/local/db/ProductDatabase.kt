package com.example.mustmarket.features.home.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mustmarket.features.home.data.local.converters.ProductConverters
import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.data.local.entities.CategoryListingEntity
import com.example.mustmarket.features.home.data.local.entities.ProductListingEntity

@Database(
    entities = [ProductListingEntity::class, CategoryListingEntity::class, BookmarkedProduct::class],
    version = 4,
    exportSchema = false
)


@TypeConverters(ProductConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val categoryDao: CategoryDao
    abstract val bookmarkDao: BookmarkDao
}