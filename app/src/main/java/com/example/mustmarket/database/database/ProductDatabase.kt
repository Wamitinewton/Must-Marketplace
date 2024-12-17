package com.example.mustmarket.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mustmarket.database.converters.DataConverters
import com.example.mustmarket.database.dao.BookmarkDao
import com.example.mustmarket.database.dao.CategoryDao
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.database.dao.UserDao
import com.example.mustmarket.database.entities.BookmarkedProductEntity
import com.example.mustmarket.database.entities.CategoryListingEntity
import com.example.mustmarket.database.entities.ProductListingEntity
import com.example.mustmarket.database.entities.UserEntity


var version:Int = 0


@Database(
    entities = [ProductListingEntity::class, CategoryListingEntity::class, BookmarkedProductEntity::class, UserEntity::class],
    version = 4,
    exportSchema = true
)
@TypeConverters(DataConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val categoryDao: CategoryDao
    abstract val bookmarkDao: BookmarkDao
    abstract val userDao: UserDao
}

