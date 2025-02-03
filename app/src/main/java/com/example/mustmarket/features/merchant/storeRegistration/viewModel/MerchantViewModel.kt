package com.example.mustmarket.features.merchant.storeRegistration.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.features.merchant.storeRegistration.mDataStore.MerchantPreferences
import com.example.mustmarket.features.merchant.storeRegistration.mDataStore.StoreProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MerchantViewModel(application: Application) : AndroidViewModel(application) {
    private val _isMerchant = MutableStateFlow(false)
    val isMerchant = _isMerchant.asStateFlow()

    private val _isMerchantId = MutableStateFlow<String?>(null)
    val merchantId = _isMerchantId.asStateFlow()

    private val _storeProfile = MutableStateFlow<StoreProfile?>(null)
    val storeProfile: StateFlow<StoreProfile?> = _storeProfile.asStateFlow()

    init {
        viewModelScope.launch {
            MerchantPreferences.isMerchant(application).collect { merchantStatus ->
                _isMerchant.value = merchantStatus
            }
        }

        viewModelScope.launch {
            MerchantPreferences.getMerchantId(application).collect { id ->
                _isMerchantId.value = id
            }
        }

        _storeProfile.value = storeProfile.value
    }

    fun setMerchantStatus(
        isMerchant: Boolean,
        merchantId: String?
    ) {
        viewModelScope.launch {
            MerchantPreferences.setMerchant(getApplication(),isMerchant,merchantId)
        }
    }

    fun setStoreProfile(storeProfile: StoreProfile) {
        _storeProfile.value = storeProfile
    }

}