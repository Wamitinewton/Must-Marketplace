package com.newton.mustmarket.features.home.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface SearchProductsRepository {
    suspend fun searchProducts(query: String): Flow<Resource<List<NetworkProduct>>>

    suspend fun searchBookmarks(query: String): Flow<Resource<List<NetworkProduct>>>
}