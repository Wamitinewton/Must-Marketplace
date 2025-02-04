package com.newton.mustmarket.features.home.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface AllProductsRepository {
    suspend fun shouldRefresh(): Boolean

    suspend fun getAllProducts(forceRefresh: Boolean): Flow<Resource<List<NetworkProduct>>>

    suspend fun getProductsById(productId: Int): Flow<Resource<NetworkProduct>>

    suspend fun clearCache()

}