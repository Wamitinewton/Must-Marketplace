package com.newton.mustmarket.features.get_products.presentation.event

sealed class SearchProductEvent {
    data class QueryChanged(val query: String): SearchProductEvent()
    data object ClearSearch: SearchProductEvent()
    data object PerformSearch: SearchProductEvent()
}