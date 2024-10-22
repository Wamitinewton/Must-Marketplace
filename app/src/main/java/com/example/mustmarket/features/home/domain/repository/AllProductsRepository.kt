package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.AllNetworkProduct
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface AllProductsRepository {
    suspend fun getAllProducts(): Flow<Resource<List<NetworkProduct>>>
}