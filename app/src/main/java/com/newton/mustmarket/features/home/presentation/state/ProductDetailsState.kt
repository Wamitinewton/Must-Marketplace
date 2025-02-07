package com.newton.mustmarket.features.home.presentation.state

import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct

sealed class    ProductDetailsState {
    data object Loading : ProductDetailsState()
    data class Success(val product: NetworkProduct) : ProductDetailsState()
    data class Error(val message: String) : ProductDetailsState()
}

