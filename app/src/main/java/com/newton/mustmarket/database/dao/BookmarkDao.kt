package com.newton.mustmarket.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newton.mustmarket.database.entities.BookmarkedProductEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkProduct(product: BookmarkedProductEntity)

    @Delete
    suspend fun removeBookmark(product: BookmarkedProductEntity)

    @Query("SELECT * FROM bookmarked_products ORDER BY bookmarkedAt DESC")
    fun getBookmarkedProducts(): Flow<List<BookmarkedProductEntity>>

    @Query("SELECT * FROM bookmarked_products WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == name")
    suspend fun searchBookmarks(query: String): List<BookmarkedProductEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_products WHERE id = :productId)")
    fun isProductBookmarked(productId: Int): Flow<Boolean>
}