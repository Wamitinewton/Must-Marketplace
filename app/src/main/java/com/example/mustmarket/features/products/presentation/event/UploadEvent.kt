package com.example.mustmarket.features.products.presentation.event

import android.content.Context
import android.net.Uri
import com.example.mustmarket.features.products.domain.models.UploadProductRequest

sealed class UploadEvent {
    data class SingleImageUpload(val uri: Uri, val context: Context): UploadEvent()
    data class MultipleImagesUpload(val uris: List<Uri>, val context: Context): UploadEvent()
    data class ProductUpload(val product: UploadProductRequest): UploadEvent()
    data object ClearError: UploadEvent()
}