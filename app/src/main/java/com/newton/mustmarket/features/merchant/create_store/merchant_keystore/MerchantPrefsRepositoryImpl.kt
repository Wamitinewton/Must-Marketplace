package com.newton.mustmarket.features.merchant.create_store.merchant_keystore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MerchantPrefsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): MerchantPrefsRepository {
    override suspend fun setMerchantStatus(isMerchant: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_MERCHANT] = isMerchant
        }
    }

    override fun getMerchantStatus(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[PreferencesKeys.IS_MERCHANT] ?: false
        }
    }
}