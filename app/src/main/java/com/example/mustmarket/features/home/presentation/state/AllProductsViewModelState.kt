package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.features.home.domain.model.NetworkProduct

data class AllProductsViewModelState(
    val isLoading: Boolean = false,
    val products: List<NetworkProduct> = emptyList(),
    val errorMessage: String = ""
)
