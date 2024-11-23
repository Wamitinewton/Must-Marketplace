package com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data

sealed class UploadError(val messages: String) {
    class S3Error(message: String, val statusCode: Int? = null) : UploadError(message)
    class NetworkError(message: String) : UploadError(message)
    class PermissionError(message: String) : UploadError(message)
    class InvalidFileError(message: String) : UploadError(message)
    class StorageError(message: String) : UploadError(message)
    class UnknownError(message: String) : UploadError(message)
}