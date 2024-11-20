package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.AllNetworkProduct
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface AllProductsRepository {
    suspend fun shouldRefresh(): Boolean

    suspend fun processAndCacheProducts(
        rawProducts: List<NetworkProduct>
    ): Resource<List<NetworkProduct>>

    suspend fun fetchAndCacheProducts(): Resource<List<NetworkProduct>>

    suspend fun getAllProducts(forceRefresh: Boolean): Flow<Resource<List<NetworkProduct>>>

    suspend fun getProductsById(productId: Int): Flow<Resource<NetworkProduct>>


    suspend fun refreshProducts(): Flow<Resource<List<NetworkProduct>>>
}