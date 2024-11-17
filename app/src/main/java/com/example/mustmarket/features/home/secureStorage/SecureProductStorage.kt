package com.example.mustmarket.features.home.secureStorage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.mustmarket.features.home.secureStorage.StorageKeys.PREF_FILE_NAME
import com.example.mustmarket.features.home.secureStorage.StorageKeys.PREF_LAST_UPDATE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
class SecureProductStorage(
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

    fun getLastUpdateTimestamp(): Long {
        return securePreferences.getLong(PREF_LAST_UPDATE, 0)
    }

    fun updateLastUpdateTimestamp() {
        securePreferences.edit()
            .putLong(PREF_LAST_UPDATE, System.currentTimeMillis())
            .apply()
    }

    fun clearStorage() {
        securePreferences.edit().clear().apply()
    }
}