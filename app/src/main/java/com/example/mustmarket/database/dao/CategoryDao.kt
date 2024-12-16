package com.example.mustmarket.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.mustmarket.database.entities.CategoryListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Upsert
    suspend fun insertCategories(categories: List<CategoryListingEntity>)

    @Query("SELECT * FROM category ORDER BY id DESC")
    fun getAllCategories(): Flow<List<CategoryListingEntity>>

    @Query("DELETE FROM category")
    suspend fun clearAllCategory()

    @Query("SELECT COUNT(*) FROM category")
    suspend fun getCategoryCount(): Int
}