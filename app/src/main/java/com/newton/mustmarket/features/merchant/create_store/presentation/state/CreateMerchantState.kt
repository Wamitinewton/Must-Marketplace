package com.newton.mustmarket.features.merchant.create_store.presentation.state

import com.newton.mustmarket.features.merchant.create_store.data.remote.response.MerchantResponseData

data class CreateMerchantState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val responseData: MerchantResponseData? = null,
    val uploadBannerAndProfileState: UploadBannerAndProfileState = UploadBannerAndProfileState(),
    val merchantDetailsInput: MerchantDetailsInput = MerchantDetailsInput(),
)

data class MerchantDetailsInput(
    val merchantName: String = "",
    val shopDescription: String = "",
    val location: String = "",
    val userId: String = "",
    val profileUrl: String = "",
    val bannerUrl: String = ""
)

data class UploadBannerAndProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val multipleImagesUrl: List<String>? = null,
)



