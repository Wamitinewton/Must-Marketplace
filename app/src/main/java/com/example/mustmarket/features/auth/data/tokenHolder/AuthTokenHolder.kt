package com.example.mustmarket.features.auth.data.tokenHolder

<<<<<<< HEAD
import com.example.mustmarket.features.auth.data.datastore.SessionManager

object AuthTokenHolder {
    var accessToken: String? = null
    var refreshToken: String? = null

    fun initialize(sessionManager: SessionManager) {
        accessToken = sessionManager.fetchAccessToken()
        refreshToken = sessionManager.fetchRefreshToken()
    }
=======
object AuthTokenHolder {
    var accessToken: String? = null
    var refreshToken: String? = null
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
}