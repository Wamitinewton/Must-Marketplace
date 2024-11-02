package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.features.home.domain.model.NetworkProduct

data class ProductDetailsState(
    val isLoading: Boolean = false,
    val products: NetworkProduct? = null,
    val errorMessage: String = ""
)
