package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import com.example.mustmarket.features.home.domain.model.categories.UploadCategoryResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CategoryRepository {

    suspend fun getCategories(size: Int): Flow<Resource<List<ProductCategory>>>
    suspend fun getAllCategories(): Flow<Resource<List<ProductCategory>>>
    suspend fun refreshCategories(): Flow<Resource<List<ProductCategory>>>
    suspend fun addCategory(image: File, name: String): Flow<Resource<UploadCategoryResponse>>

}