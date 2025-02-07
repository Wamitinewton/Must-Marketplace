package com.newton.mustmarket.features.products.presentation.event

sealed class HomeScreenEvent {
    data object Refresh: HomeScreenEvent()
}