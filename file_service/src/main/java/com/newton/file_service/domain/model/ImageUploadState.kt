package com.newton.file_service.domain.model

sealed class ImageUploadState {
    data object Initial: ImageUploadState()
    data object Loading: ImageUploadState()
    data class Progress(val percentage: Int): ImageUploadState()
    data class MultipleImageSuccess(
        val imageUrls: List<String>? = null,
        val message: String? = "Upload successful",
        val timeStamp: Long? = System.currentTimeMillis()
    ): ImageUploadState()

    data class SingleImageSuccess(
        val imageUrls: String? = null,
        val message: String? = "Upload successful",
        val timeStamp: Long? = System.currentTimeMillis()
    ): ImageUploadState()

    data class Error(val message: String): ImageUploadState()
}