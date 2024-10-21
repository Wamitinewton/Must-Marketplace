package com.example.mustmarket.features.home.data.remote

import com.example.mustmarket.features.home.domain.model.AllNetworkProduct
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.model.ProductCategory
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    @GET("categories/all")
    suspend fun getCategories(
        @Query("size") size: Int
    ): List<ProductCategory>

    @GET("categories/all")
    suspend fun getAllCategories(): List<ProductCategory>

    @GET("products/all")
    suspend fun getAllProducts(): AllNetworkProduct
}