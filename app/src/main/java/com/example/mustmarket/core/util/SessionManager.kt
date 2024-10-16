package com.example.mustmarket.core.util

import android.content.Context
import android.content.SharedPreferences
import com.example.mustmarket.R
import com.example.mustmarket.core.util.Constants.ACCESS_TOKEN
import com.example.mustmarket.core.util.Constants.REFRESH_TOKEN

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    // saving the access tokens

    fun saveAccessToken(token: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    // function to fetch the access tokens
    fun fetchAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun saveRefreshToken(token: String) {
        val editor = prefs.edit()
        editor.putString(REFRESH_TOKEN, token)
        editor.apply()
    }

    fun fetchRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }
}