package com.example.mustmarket.features.auth.workmanager

import android.content.Context
import android.util.Log
import com.example.mustmarket.features.auth.datastore.DatastoreKeys.KEY_ACCESS_TOKEN
import com.example.mustmarket.features.auth.datastore.DatastoreKeys.KEY_REFRESH_TOKEN
import com.example.mustmarket.features.auth.datastore.SessionManager
import com.example.mustmarket.features.auth.workmanager.HeadersManager.AUTHORIZATION_HEADER
import com.example.mustmarket.features.auth.workmanager.HeadersManager.BEARER_PREFIX
import com.example.mustmarket.features.auth.workmanager.HeadersManager.NEW_ACCESS_TOKEN_HEADER
import com.example.mustmarket.features.auth.workmanager.HeadersManager.REFRESH_HEADER
import com.example.mustmarket.features.auth.workmanager.HeadersManager.TAG
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAccessToken()?.let {
            requestBuilder.addHeader(AUTHORIZATION_HEADER, "$BEARER_PREFIX $it")
        }

        sessionManager.fetchRefreshToken()?.let {
            requestBuilder.addHeader(REFRESH_HEADER, it)
        }

        val response = chain.proceed(requestBuilder.build())
        getTokensFromResHeaders(response)
        return response
    }

    private fun getTokensFromResHeaders(response: Response) {
        var accessToken = response.headers[KEY_ACCESS_TOKEN]
        val refreshToken = response.headers[KEY_REFRESH_TOKEN]
        val newAccessToken = response.headers[NEW_ACCESS_TOKEN_HEADER]
        if (newAccessToken != null) accessToken = newAccessToken
        if (accessToken != null && refreshToken != null) {
            sessionManager.saveAccessToken(accessToken)
            sessionManager.saveRefreshToken(refreshToken)
            Log.d(TAG, "access: $accessToken")
        } else {
            Log.d(TAG, "Not token ${response.headers}")
        }
    }
}