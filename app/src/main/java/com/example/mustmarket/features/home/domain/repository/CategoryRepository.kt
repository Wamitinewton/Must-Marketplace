package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategories(size: Int): Flow<Resource<List<Category>>>
    suspend fun getAllCategories(): Flow<Resource<List<Category>>>
}