package com.example.mustmarket.features.home.data.remote

import com.example.mustmarket.features.home.data.remote.dto.AllProductsDto
import com.example.mustmarket.features.home.data.remote.dto.CategoryResponseDto
import com.example.mustmarket.features.home.data.remote.dto.ProductDetailsDto
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import com.example.mustmarket.features.home.domain.model.categories.UploadCategoryResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {

    @GET("categories/all")
    suspend fun getCategories(
        @Query("size") size: Int
    ): List<ProductCategory>

    @GET("api/v1/products/get/{id}")
    suspend fun getProductsById(
        @Path("id") productId: Int
    ): ProductDetailsDto

    @GET("api/v1/categories/all")
    suspend fun getAllCategories(): CategoryResponseDto

    @GET("api/v1/products/all")
    suspend fun getAllProducts(
    ): AllProductsDto

    @GET("api/v1/products/by-name/{query}")
    suspend fun searchProducts(
        @Query("query") query: String
    ): AllProductsDto

    @Multipart
    @POST("api/v1/categories/add")
    suspend fun addCategory(
        @Query("name") name: String,
        @Part image: MultipartBody.Part
    ): UploadCategoryResponse

}