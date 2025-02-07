package com.newton.mustmarket.features.get_products.presentation.state

import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct

data class AllProductsViewModelState(
    val isLoading: Boolean = false,
    val products: List<NetworkProduct> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)

data class GetProductsByCategoryState(
    val isLoading: Boolean = false,
    val products: List<NetworkProduct> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)
