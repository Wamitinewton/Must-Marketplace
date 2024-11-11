package com.example.mustmarket.core.networkManager

sealed class NetworkConnectionState {
    data object Available : NetworkConnectionState()
    data object Unavailable : NetworkConnectionState()
}