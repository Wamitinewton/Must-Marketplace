package com.newton.mustmarket.features.merchant.create_store.presentation.event

import android.content.Context
import android.net.Uri
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest

sealed class CreateMerchantEvent {
    data class BannerAndProfileUpload(val bannerUri: Uri, val profileUri: Uri ,val context: Context, val createMerchantRequest: CreateMerchantRequest): CreateMerchantEvent()
    data object ClearError: CreateMerchantEvent()
    data class MerchantNameChanged(val name: String): CreateMerchantEvent()
    data class ShopDescriptionChanged(val description: String): CreateMerchantEvent()
    data class ShopLocationChanged(val location: String): CreateMerchantEvent()
}