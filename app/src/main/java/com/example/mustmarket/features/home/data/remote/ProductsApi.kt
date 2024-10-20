package com.example.mustmarket.features.home.data.remote

import com.example.mustmarket.features.home.domain.model.Category
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    @GET("categories/all")
    suspend fun getCategories(
        @Query("size") size: Int
    ): List<Category>

    @GET("categories/all")
    suspend fun getAllCategories(): List<Category>
}