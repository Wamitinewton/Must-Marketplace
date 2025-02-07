package com.newton.mustmarket.features.get_products.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.get_products.domain.model.categories.ProductCategory
import com.newton.mustmarket.features.get_products.domain.model.categories.UploadCategoryResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CategoryRepository {

    suspend fun getCategories(size: Int): Flow<Resource<List<ProductCategory>>>
    suspend fun getAllCategories(shouldRefresh: Boolean): Flow<Resource<List<ProductCategory>>>
    suspend fun addCategory(image: File, name: String): Flow<Resource<UploadCategoryResponse>>
    suspend fun shouldRefresh(): Boolean

}