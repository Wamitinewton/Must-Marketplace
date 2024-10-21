package com.example.mustmarket.features.home.presentation

import com.example.mustmarket.features.home.domain.model.ProductCategory

data class ProductCategoryViewModelState(
    val isLoading: Boolean = false,
    val categories: List<ProductCategory> = emptyList(),
    val errorMessage: String = ""
)