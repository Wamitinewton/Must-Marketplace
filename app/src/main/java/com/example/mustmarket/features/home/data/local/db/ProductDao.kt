package com.example.mustmarket.features.home.data.local.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.mustmarket.features.home.data.local.entities.ProductListingEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {

    @Upsert
    suspend fun insertProducts(products: List<ProductListingEntity>)

    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProducts(): Flow<List<ProductListingEntity>>

    @Query("DELETE FROM products")
    suspend fun clearAllProducts()

    @Query("SELECT * FROM products WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == name")
    suspend fun searchProducts(query: String): List<ProductListingEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductListingEntity?

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

}