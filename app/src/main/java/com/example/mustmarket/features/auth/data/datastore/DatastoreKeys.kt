package com.example.mustmarket.features.auth.data.datastore

object DatastoreKeys {
    const val PREF_FILE_NAME = "secure_session_prefs"
    const val KEY_ACCESS_TOKEN = "access_token"
    const val KEY_REFRESH_TOKEN = "refresh_token"
    const val KEY_TOKEN_EXPIRY = "token_expiry"
    const val DEFAULT_TOKEN_EXPIRY_HOURS = 240L

    const val USER_CACHE_PREFS = "user_preferences"
    const val USERDATA = "user_data"
}