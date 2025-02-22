package com.newton.mustmarket.features.get_products.presentation.state

import com.newton.mustmarket.features.get_products.domain.model.categories.ProductCategory

data class ProductCategoryViewModelState(
    val isLoading: Boolean = false,
    val categories: List<ProductCategory> = emptyList(),
    val allCategories: List<ProductCategory> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String = "",
    val addCategoryState: AddCategoryState = AddCategoryState()
)

data class AddCategoryState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)