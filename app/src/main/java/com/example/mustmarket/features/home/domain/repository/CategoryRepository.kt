package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.ApiCategoryResponse
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.model.ProductCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategories(size: Int): Flow<Resource<List<ProductCategory>>>
    suspend fun getAllCategories(): Flow<Resource<List<ProductCategory>>>
    suspend fun refreshCategories(): Flow<Resource<List<ProductCategory>>>
}