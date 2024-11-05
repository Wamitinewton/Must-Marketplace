package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface SearchProductsRepository {
    suspend fun searchProducts(query: String): Flow<Resource<List<NetworkProduct>>>

    suspend fun searchBookmarks(query: String): Flow<Resource<List<NetworkProduct>>>
}