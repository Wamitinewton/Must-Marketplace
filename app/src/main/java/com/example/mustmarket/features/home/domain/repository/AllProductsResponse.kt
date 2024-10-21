package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.AllNetworkProduct
import kotlinx.coroutines.flow.Flow

interface AllProductsResponse {
    suspend fun getAllProducts(): Flow<Resource<AllNetworkProduct>>
}