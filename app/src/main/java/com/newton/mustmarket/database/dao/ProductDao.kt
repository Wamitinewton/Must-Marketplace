package com.newton.mustmarket.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.newton.mustmarket.database.entities.ProductListingEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {

    @Upsert
    suspend fun insertProducts(products: List<ProductListingEntity>)

    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProducts(): Flow<List<ProductListingEntity>>

    @Query("SELECT * FROM products ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun getPagedProducts(limit: Int, offset: Int): Flow<List<ProductListingEntity>>

    @Query("DELETE FROM products")
    suspend fun clearAllProducts()

    @Query("SELECT * FROM products WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == name")
    suspend fun searchProducts(query: String): List<ProductListingEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductListingEntity?

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

}