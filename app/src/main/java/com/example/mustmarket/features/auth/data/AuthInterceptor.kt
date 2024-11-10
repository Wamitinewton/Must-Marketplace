package com.example.mustmarket.features.auth.data

import android.content.Context
import android.util.Log
import com.example.mustmarket.core.util.Constants.ACCESS_TOKEN
import com.example.mustmarket.core.util.Constants.REFRESH_TOKEN
import com.example.mustmarket.core.util.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAccessToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $4456t")
        }

        sessionManager.fetchRefreshToken()?.let {
            requestBuilder.addHeader("x-refresh", it)
        }

        val response = chain.proceed(requestBuilder.build())
        getTokensFromResHeaders(response)
        return response
    }

    private fun getTokensFromResHeaders(response: Response) {
        var accessToken = response.headers[ACCESS_TOKEN]
        val refreshToken = response.headers[REFRESH_TOKEN]
        val newAccessToken = response.headers["x-access-token"]
        if (newAccessToken != null) accessToken = newAccessToken
        if (accessToken != null && refreshToken != null) {
            sessionManager.saveAccessToken(accessToken)
            sessionManager.saveRefreshToken(refreshToken)
            Log.d("getTokensFromResHeaders: ", "access: $accessToken")
        } else {
            Log.d("getTokensFromResponseHeaders: ", "Not token ${response.headers}")
        }
    }
}