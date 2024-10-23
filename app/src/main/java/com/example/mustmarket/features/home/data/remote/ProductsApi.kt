package com.example.mustmarket.features.home.data.remote

import com.example.mustmarket.features.home.data.remote.dto.AllProductsDto
import com.example.mustmarket.features.home.data.remote.dto.CategoryResponseDto
import com.example.mustmarket.features.home.domain.model.ProductCategory
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    @GET("categories/all")
    suspend fun getCategories(
        @Query("size") size: Int
    ): List<ProductCategory>

    @GET("api/v1/categories/all")
    suspend fun getAllCategories(): CategoryResponseDto

    @GET("api/v1/products/all")
    suspend fun getAllProducts(): AllProductsDto
}