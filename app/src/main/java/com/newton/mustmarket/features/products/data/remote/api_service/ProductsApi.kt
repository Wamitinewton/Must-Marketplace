package com.newton.mustmarket.features.products.data.remote.api_service

import com.newton.mustmarket.features.products.data.remote.response.AllProductsDto
import com.newton.mustmarket.features.products.data.remote.response.CategoryResponseDto
import com.newton.mustmarket.features.products.data.remote.response.ProductDetailsDto
import com.newton.mustmarket.features.products.domain.model.categories.ProductCategory
import com.newton.mustmarket.features.products.domain.model.categories.UploadCategoryResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {

    @GET(ProductsEndpoints.GET_CATEGORY_ID)
    suspend fun getCategories(
        @Query("size") size: Int
    ): List<ProductCategory>

    @GET(ProductsEndpoints.GET_PRODUCTS_BY_ID)
    suspend fun getProductsById(
        @Path("id") productId: Int
    ): ProductDetailsDto

    @GET(ProductsEndpoints.GET_ALL_CATEGORIES)
    suspend fun getAllCategories(): CategoryResponseDto

    @GET(ProductsEndpoints.GET_ALL_PRODUCTS)
    suspend fun getAllProducts(
    ): AllProductsDto

    @GET(ProductsEndpoints.SEARCH_PRODUCTS)
    suspend fun searchProducts(
        @Query("query") query: String
    ): AllProductsDto

    @Multipart
    @POST(ProductsEndpoints.ADD_CATEGORY)
    suspend fun addCategory(
        @Query("name") name: String,
        @Part image: MultipartBody.Part
    ): UploadCategoryResponse

}