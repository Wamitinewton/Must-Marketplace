package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.features.home.domain.model.products.NetworkProduct

data class AllProductsViewModelState(
    val isLoading: Boolean = false,
    val products: List<NetworkProduct> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)
