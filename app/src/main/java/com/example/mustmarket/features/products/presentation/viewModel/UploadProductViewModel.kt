package com.example.mustmarket.features.products.presentation.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.products.presentation.event.UploadEvent
import com.example.mustmarket.features.products.presentation.state.UploadProductUiState
import com.example.mustmarket.features.products.presentation.state.UploadSingleImageUrlState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UploadProductViewModel @Inject constructor(
    private val productUseCases: UseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(UploadProductUiState())
    val uiState: StateFlow<UploadProductUiState> = _uiState.asStateFlow()

    fun handleEvent(event: UploadEvent) {
        when (event) {
            UploadEvent.ClearError -> clearError()
            is UploadEvent.MultipleImagesUpload -> uploadImageList(event.context, event.uris)
            is UploadEvent.ProductUpload -> {
                if (validateProduct(event.product)) {
                    addProduct(event.product)
                }
            }

            is UploadEvent.SingleImageUpload -> uploadSingleImage(event.uri, event.context)
        }
    }

    private fun validateProduct(product: UploadProductRequest): Boolean {
        if (product.images.isEmpty()) {
            handleError("Please upload at least one image")
            return false
        }
        if (product.name.isBlank()) {
            handleError("Product name cannot be empty")
            return false
        }
        if (product.price.isBlank()) {
            handleError("Product price cannot be empty")
            return false
        }
        if (product.description.isBlank()) {
            handleError("Product description cannot be empty")
            return false
        }
        return true
    }

    private fun uploadSingleImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                updateLoadingState(true)
                val file = uriToFile(context, uri)
                productUseCases.addProduct.uploadSingleImages(file)
                    .collect { response ->
                        when (response) {
                            is Resource.Error -> {
                                handleError(response.message ?: "Failed to upload image")
                                updateLoadingState(false)
                            }

                            is Resource.Loading -> {
                                _uiState.update { it.copy(
                                    uploadSingleImageState = UploadSingleImageUrlState(
                                        isLoading = true,
                                        singleImageUrl = ""
                                    )
                                ) }
                            }
                            is Resource.Success -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        isLoading = false,
                                        singleImageUrl = response.data?.data.orEmpty()
                                    )
                                }
                                updateLoadingState(false)
                            }
                        }
                    }
            } catch (e: IOException) {
                handleError("Failed to process image: ${e.localizedMessage}")
                updateLoadingState(false)
            }
        }
    }

    private fun uploadImageList(context: Context, uri: List<Uri>) {
        viewModelScope.launch {
            try {
                updateLoadingState(true)
                val files = uri.map { uriToFile(context, it) }
                productUseCases.addProduct.uploadListOfImages(files)
                    .collect { response ->
                        when (response) {
                            is Resource.Error -> {
                                handleError(response.message ?: "Failed to upload images")
                                updateLoadingState(false)
                            }

                            is Resource.Loading -> updateLoadingState(true)
                            is Resource.Success -> {
                                if (response.data?.data.isNullOrEmpty()) {
                                    handleError("No images were uploaded")
                                } else {
                                    _uiState.update { currentState ->
                                        currentState.copy(
                                            isLoading = false,
                                            imageUrls = response.data?.data.orEmpty()
                                        )
                                    }
                                }
                                updateLoadingState(false)
                            }
                        }
                    }
            } catch (e: IOException) {
                handleError("Failed to process images: ${e.localizedMessage}")
                updateLoadingState(false)
            }
        }
    }

    private fun addProduct(product: UploadProductRequest) {
        viewModelScope.launch {
            try {
                updateLoadingState(true)
                productUseCases.addProduct.addProduct(product).collect { response ->
                    when (response) {
                        is Resource.Loading -> updateLoadingState(true)
                        is Resource.Success -> {
                            if (response.data?.data == null) {
                                handleError("Failed to add product: No response from server")
                            } else {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        isLoading = false,
                                        uploadData = response.data.data,
                                        error = null
                                    )
                                }
                            }
                            updateLoadingState(false)
                        }

                        is Resource.Error -> {
                            handleError(response.message ?: "Failed to add product")
                            updateLoadingState(false)
                        }
                    }
                }
            } catch (e: Exception) {
                handleError("Failed to add product: ${e.localizedMessage}")
                updateLoadingState(false)
            }
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun handleError(error: String) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                error = error
            )
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val file = File(
                context.cacheDir,
                "upload_${System.currentTimeMillis()}.jpg"
            )
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return file
        } ?: throw IOException("Failed to open input stream from URI: $uri")
    }
}