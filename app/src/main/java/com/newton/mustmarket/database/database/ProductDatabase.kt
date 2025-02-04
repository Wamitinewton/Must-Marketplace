package com.newton.mustmarket.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newton.mustmarket.database.converters.DataConverters
import com.newton.mustmarket.database.dao.BookmarkDao
import com.newton.mustmarket.database.dao.CategoryDao
import com.newton.mustmarket.database.dao.ProductDao
import com.newton.mustmarket.database.dao.UserDao
import com.newton.mustmarket.database.entities.BookmarkedProductEntity
import com.newton.mustmarket.database.entities.CategoryListingEntity
import com.newton.mustmarket.database.entities.ProductListingEntity
import com.newton.mustmarket.database.entities.UserEntity
import com.newton.mustmarket.features.inbox.chat.model.ChatDao
import com.newton.mustmarket.features.inbox.chat.model.ChatEntity
import com.newton.mustmarket.features.inbox.chat.model.ChatMessageEntity


var version:Int = 0


@Database(
    entities = [ProductListingEntity::class, CategoryListingEntity::class, BookmarkedProductEntity::class, UserEntity::class, ChatMessageEntity::class, ChatEntity::class],
    version = 5,
    exportSchema = true
)
@TypeConverters(DataConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val categoryDao: CategoryDao
    abstract val bookmarkDao: BookmarkDao
    abstract val userDao: UserDao
    abstract val chatDao: ChatDao
}

