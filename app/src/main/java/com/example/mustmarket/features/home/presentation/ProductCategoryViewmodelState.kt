package com.example.mustmarket.features.home.presentation

import com.example.mustmarket.features.product.domain.model.Category

data class ProductCategoryViewModelState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val errorMessage: String = ""
)