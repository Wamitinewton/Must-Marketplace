package com.newton.mustmarket.features.home.presentation.event

sealed class SearchProductEvent {
    data class QueryChanged(val query: String): SearchProductEvent()
    data object ClearSearch: SearchProductEvent()
    data object PerformSearch: SearchProductEvent()
}