package com.example.mustmarket.features.merchant.storeRegistration.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.application.MustApplication
import com.example.mustmarket.features.merchant.storeRegistration.mDataStore.MerchantPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MerchantViewModel(application: Application) : AndroidViewModel(application) {
    private val _isMerchant = MutableStateFlow(false)
    val isMerchant = _isMerchant.asStateFlow()

    private val _isMerchantId = MutableStateFlow<String?>(null)
    val merchantId = _isMerchantId.asStateFlow()

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
    }

    fun setMerchantStatus(
        isMerchant: Boolean,
        merchantId: String?
    ) {
        viewModelScope.launch {
            MerchantPreferences.setMerchant(getApplication(),isMerchant,merchantId)
        }
    }

}