package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.features.home.domain.model.products.NetworkProduct

data class SearchProductsState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<NetworkProduct> = emptyList(),
    val errorMessage: String? = null,
    val isSearchActive: Boolean = false
)