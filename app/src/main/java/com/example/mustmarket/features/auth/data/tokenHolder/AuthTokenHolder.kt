package com.example.mustmarket.features.auth.data.tokenHolder

import com.example.mustmarket.features.auth.data.datastore.SessionManager

object AuthTokenHolder {
    var accessToken: String? = null
    var refreshToken: String? = null

    fun initialize(sessionManager: SessionManager) {
        accessToken = sessionManager.fetchAccessToken()
        refreshToken = sessionManager.fetchRefreshToken()
    }
}