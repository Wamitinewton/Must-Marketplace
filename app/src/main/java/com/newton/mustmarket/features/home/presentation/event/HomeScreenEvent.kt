package com.newton.mustmarket.features.home.presentation.event

sealed class HomeScreenEvent {
    data object Refresh: HomeScreenEvent()
}