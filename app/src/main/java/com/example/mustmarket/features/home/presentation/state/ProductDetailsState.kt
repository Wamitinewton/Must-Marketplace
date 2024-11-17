package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.features.home.domain.model.NetworkProduct

sealed class    ProductDetailsState {
    data object Loading : ProductDetailsState()
    data class Success(val product: NetworkProduct) : ProductDetailsState()
    data class Error(val message: String) : ProductDetailsState()
}

