package com.newton.mustmarket.features.merchant.store.mDataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "merchant_prefs")

object MerchantPreferences {
    private val IS_MERCHANT_KEY = booleanPreferencesKey("is_merchant")
    private val MERCHANT_ID_KEY = stringPreferencesKey("merchant_id")

    fun isMerchant(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_MERCHANT_KEY] ?: false
        }
    }

    fun getMerchantId(context: Context): Flow<String?> {
        return context.dataStore.data.map { preference ->
            preference[MERCHANT_ID_KEY]
        }
    }

    suspend fun setMerchant(
        context: Context,
        isMerchant: Boolean,
        merchantId: String?
    ) {
        context.dataStore.edit { preference ->
            preference[IS_MERCHANT_KEY] = isMerchant
            if (merchantId != null) {
            preference[MERCHANT_ID_KEY] = merchantId
            }
        }
    }

}