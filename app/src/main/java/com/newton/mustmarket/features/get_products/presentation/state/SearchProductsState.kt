package com.newton.mustmarket.features.get_products.presentation.state

import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct

data class SearchProductsState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<NetworkProduct> = emptyList(),
    val errorMessage: String? = null,
    val isSearchActive: Boolean = false
)