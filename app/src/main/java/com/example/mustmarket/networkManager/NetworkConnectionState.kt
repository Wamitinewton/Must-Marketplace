package com.example.mustmarket.networkManager

sealed class NetworkConnectionState {
    data object Available : NetworkConnectionState()
    data object Unavailable : NetworkConnectionState()
}