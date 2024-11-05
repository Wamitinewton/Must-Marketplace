package com.example.mustmarket.features.home.presentation.state

sealed class HomeScreenEvent {
    data object Refresh: HomeScreenEvent()
    data class Search(val query: String): HomeScreenEvent()
    data object ClearSearch: HomeScreenEvent()
}