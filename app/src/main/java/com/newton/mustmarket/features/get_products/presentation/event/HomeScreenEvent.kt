package com.newton.mustmarket.features.get_products.presentation.event

sealed class HomeScreenEvent {
    data object Refresh: HomeScreenEvent()
    data class OnCategoryClicked(val category: String): HomeScreenEvent()
}