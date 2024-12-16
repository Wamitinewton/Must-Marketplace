package com.example.mustmarket.features.auth.data.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.mustmarket.features.auth.data.datastore.DatastoreKeys.DEFAULT_TOKEN_EXPIRY_HOURS
import com.example.mustmarket.features.auth.data.datastore.DatastoreKeys.KEY_ACCESS_TOKEN
import com.example.mustmarket.features.auth.data.datastore.DatastoreKeys.KEY_REFRESH_TOKEN
import com.example.mustmarket.features.auth.data.datastore.DatastoreKeys.KEY_TOKEN_EXPIRY
import com.example.mustmarket.features.auth.data.datastore.DatastoreKeys.PREF_FILE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePreferences = EncryptedSharedPreferences.create(
        context,
        PREF_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    fun saveAccessToken(token: String) {
        securePreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, token)
            putLong(KEY_TOKEN_EXPIRY, calculateTokenExpiry())
            apply()
        }
    }

    fun fetchAccessToken(): String? {
        return if (isTokenExpired()) {
            null
        } else {
            securePreferences.getString(KEY_ACCESS_TOKEN, null)
        }
    }

    fun saveRefreshToken(token: String) {
        securePreferences.edit().apply {
            putString(KEY_REFRESH_TOKEN, token)
            apply()
        }
    }

    fun fetchRefreshToken(): String? {
        return securePreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        securePreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putLong(KEY_TOKEN_EXPIRY, calculateTokenExpiry())
            apply()
        }
    }

    fun updateTokens(accessToken: String?, refreshToken: String?, expiryHours: Long? = null) {
        securePreferences.edit().apply {
            accessToken?.let { putString(KEY_ACCESS_TOKEN, it) }
            refreshToken?.let { putString(KEY_REFRESH_TOKEN, it) }
            expiryHours?.let { putLong(KEY_TOKEN_EXPIRY, calculateTokenExpiry()) }
            apply()
        }
    }

    fun clearTokens() {
        securePreferences.edit().clear().apply()
    }

    fun isSessionValid(): Boolean {
        return fetchAccessToken() != null && !isTokenExpired()
    }

    private fun calculateTokenExpiry(
        expiryHours: Long = DEFAULT_TOKEN_EXPIRY_HOURS
    ): Long {
        return System.currentTimeMillis() + TimeUnit.HOURS.toMillis(expiryHours)
    }

    private fun isTokenExpired(): Boolean {
        val expiryTime = securePreferences.getLong(KEY_TOKEN_EXPIRY, 0)
        return System.currentTimeMillis() >= expiryTime
    }

    fun updateTokenExpiry(expiryHours: Long = DEFAULT_TOKEN_EXPIRY_HOURS) {
        securePreferences.edit().apply {
            putLong(KEY_TOKEN_EXPIRY, calculateTokenExpiry(expiryHours))
            apply()
        }
    }
}