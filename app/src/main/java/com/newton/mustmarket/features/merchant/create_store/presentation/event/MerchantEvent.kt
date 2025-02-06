package com.newton.mustmarket.features.merchant.create_store.presentation.event

import android.content.Context
import android.net.Uri
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest

sealed class MerchantEvent {
    data class BannerAndProfileUpload(val bannerUri: Uri, val profileUri: Uri ,val context: Context, val createMerchantRequest: CreateMerchantRequest): MerchantEvent()
    data object ClearError: MerchantEvent()
    data class MerchantNameChanged(val name: String): MerchantEvent()
    data class ShopDescriptionChanged(val description: String): MerchantEvent()
    data class ShopLocationChanged(val location: String): MerchantEvent()
}