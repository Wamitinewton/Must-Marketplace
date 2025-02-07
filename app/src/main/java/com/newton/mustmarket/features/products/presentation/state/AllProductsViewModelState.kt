package com.newton.mustmarket.features.products.presentation.state

import com.newton.mustmarket.features.products.domain.model.products.NetworkProduct

data class AllProductsViewModelState(
    val isLoading: Boolean = false,
    val products: List<NetworkProduct> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)
