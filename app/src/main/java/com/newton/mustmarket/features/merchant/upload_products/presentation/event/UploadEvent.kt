package com.newton.mustmarket.features.merchant.upload_products.presentation.event

import android.content.Context
import android.net.Uri
import com.newton.mustmarket.features.merchant.upload_products.domain.models.UploadProductRequest

sealed class UploadEvent {
    data class SingleImageUpload(val uri: Uri, val context: Context): UploadEvent()
    data class MultipleImagesUpload(val uris: List<Uri>, val context: Context, val product: UploadProductRequest): UploadEvent()
    data object ClearError: UploadEvent()
    data class ProductNameChanged(val name: String): UploadEvent()
    data class ProductDescriptionChanged(val description: String): UploadEvent()
    data class ProductPriceChanged(val price: Int): UploadEvent()
    data class ProductCategoryChanged(val category: String): UploadEvent()
    data class ProductBrandChanged(val brand: String): UploadEvent()
    data class ProductInventoryChanged(val inventory: Int): UploadEvent()
}