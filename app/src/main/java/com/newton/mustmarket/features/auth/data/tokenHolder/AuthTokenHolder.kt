package com.newton.mustmarket.features.auth.data.tokenHolder

import com.newton.mustmarket.features.auth.data.datastore.SessionManager

object AuthTokenHolder {
    var accessToken: String? = null
    var refreshToken: String? = null

    fun initializeTokens(sessionManager: SessionManager) {
        accessToken = sessionManager.fetchAccessToken()
        refreshToken = sessionManager.fetchRefreshToken()
    }
}