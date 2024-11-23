package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.features.home.domain.model.categories.ProductCategory

data class ProductCategoryViewModelState(
    val isLoading: Boolean = false,
    val categories: List<ProductCategory> = emptyList(),
    val allCategories: List<ProductCategory> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String = ""
)