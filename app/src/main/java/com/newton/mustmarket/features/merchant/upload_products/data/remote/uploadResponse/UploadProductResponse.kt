package com.newton.mustmarket.features.merchant.upload_products.data.remote.uploadResponse

import com.newton.mustmarket.features.merchant.upload_products.domain.models.UploadData
import kotlinx.serialization.Serializable

@Serializable
data class UploadProductResponse(
    val message: String,
    val data: UploadData,
)
