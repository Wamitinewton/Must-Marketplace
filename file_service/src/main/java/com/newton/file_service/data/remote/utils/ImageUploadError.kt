package com.newton.file_service.data.remote.utils

sealed class ImageUploadError {
    data class NetworkError(val message: String, val code: Int? = null): ImageUploadError()
    data class FileProcessingError(val message: String): ImageUploadError()
    data class ValidationError(val message: String): ImageUploadError()
    data class StorageError(val message: String): ImageUploadError()
}