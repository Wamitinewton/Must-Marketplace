package com.example.mustmarket.features.auth.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.mustmarket.features.auth.datastore.DatastoreKeys.USER_CACHE_PREFS
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securedSharedPrefs = EncryptedSharedPreferences.create(
        context,
        USER_CACHE_PREFS,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUserData(name: String, email: String) {
        securedSharedPrefs.edit().apply {
            putString("name", name)
            putString("email", email)
            apply()
        }
    }

    fun fetchUserData(): Pair<String?, String?> {
        val name = securedSharedPrefs.getString("name", null)
        val email = securedSharedPrefs.getString("email", null)
        return Pair(name, email)
    }

    fun clearUserData() {
        securedSharedPrefs.edit().clear().apply()
    }

}