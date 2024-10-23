package com.example.mustmarket.features.home.presentation.state

sealed class HomeScreenEvent {
    data object Refresh: HomeScreenEvent()
}