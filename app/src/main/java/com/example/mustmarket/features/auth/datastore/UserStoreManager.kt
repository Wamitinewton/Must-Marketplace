package com.example.mustmarket.features.auth.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.mustmarket.features.auth.datastore.DatastoreKeys.USERDATA
import com.example.mustmarket.features.auth.datastore.DatastoreKeys.USER_CACHE_PREFS
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStoreManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson = Gson()
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

    fun saveUserData(userData: UserData) {
        securedSharedPrefs.edit().apply {
            putString(USERDATA, gson.toJson(userData))
            apply()
        }
    }

    fun fetchUserData(): UserData? {
        val userDataJson = securedSharedPrefs.getString(USERDATA, null)
        return userDataJson?.let {
            gson.fromJson(it, UserData::class.java)
        }
    }

    fun updateUserField(update: UserData.() -> UserData) {
        val existingUserData = fetchUserData() ?: UserData()
        val updatedUserData = existingUserData.update()
        saveUserData(updatedUserData)
    }


    fun clearUserData() {
        securedSharedPrefs.edit().clear().apply()
    }

}