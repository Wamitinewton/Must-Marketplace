package com.example.mustmarket.features.home.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.data.local.entities.ProductListingEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkProduct(product: BookmarkedProduct)

    @Delete
    suspend fun removeBookmark(product: BookmarkedProduct)

    @Query("SELECT * FROM bookmarked_products ORDER BY bookmarkedAt DESC")
    fun getBookmarkedProducts(): Flow<List<BookmarkedProduct>>

    @Query("SELECT * FROM bookmarked_products WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == name")
    suspend fun searchBookmarks(query: String): List<BookmarkedProduct>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_products WHERE id = :productId)")
    fun isProductBookmarked(productId: Int): Flow<Boolean>
}