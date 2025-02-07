package com.newton.mustmarket.features.merchant.products.presentation.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newton.file_service.domain.model.ImageUploadState
import com.newton.file_service.domain.repository.ImageUploadRepository
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.products.domain.models.UploadProductRequest
import com.newton.mustmarket.features.merchant.products.presentation.event.UploadEvent
import com.newton.mustmarket.features.merchant.products.presentation.state.UploadMultipleImageState
import com.newton.mustmarket.features.merchant.products.presentation.state.UploadProductUiState
import com.newton.mustmarket.features.merchant.products.presentation.state.UploadSingleImageUrlState
import com.newton.mustmarket.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UploadProductViewModel @Inject constructor(
    private val productUseCases: UseCases,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadProductUiState())
    val uiState: StateFlow<UploadProductUiState> = _uiState.asStateFlow()

    // State used for reporting image upload progress (for both single and multiple images)
    private val _uploadProgress = MutableStateFlow<ImageUploadState>(ImageUploadState.Initial)
    val uploadProgress = _uploadProgress.asStateFlow()

    private val _navigateToMyProducts = Channel<Unit>()
    val navigateToMyProducts = _navigateToMyProducts.receiveAsFlow()

    fun handleEvent(event: UploadEvent) {
        when (event) {
            UploadEvent.ClearError -> clearError()

            is UploadEvent.MultipleImagesUpload -> {
                uploadProductWithImages(event.context, event.uris, event.product)
            }

            is UploadEvent.SingleImageUpload -> uploadSingleImage(event.uri, event.context)
            is UploadEvent.ProductBrandChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        productInput = currentState.productInput.copy(
                            productBrand = event.brand
                        )
                    )
                }
            }
            is UploadEvent.ProductCategoryChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        productInput = currentState.productInput.copy(
                            productCategory = event.category
                        )
                    )
                }
            }
            is UploadEvent.ProductDescriptionChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        productInput = currentState.productInput.copy(
                            productDescription = event.description
                        )
                    )
                }
            }
            is UploadEvent.ProductInventoryChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        productInput = currentState.productInput.copy(
                            productInventory = event.inventory
                        )
                    )
                }
            }
            is UploadEvent.ProductNameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        productInput = currentState.productInput.copy(
                            productName = event.name
                        )
                    )
                }
            }
            is UploadEvent.ProductPriceChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        productInput = currentState.productInput.copy(
                            productPrice = event.price
                        )
                    )
                }
            }
        }
    }

    /**
     * 1. Validate product details.
     * 2. Convert image URIs to files and upload to S3 (with progress reporting).
     * 3. Once image upload succeeds, add product details (with S3 image URLs) to the database.
     */
    private fun uploadProductWithImages(context: Context, uris: List<Uri>, product: UploadProductRequest) {
        viewModelScope.launch {
            // Validate the product details
            validateProductInput()?.let { errorMsg ->
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                return@launch
            }
            if (uris.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, error = "No images selected") }
                return@launch
            }

            // Convert URIs to File objects
            val files = try {
                uris.map { uriToFile(context, it) }
            } catch (e: IOException) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to process images: ${e.localizedMessage}") }
                return@launch
            }

            // Upload images to S3 while showing progress
            imageUploadRepository.uploadMultipleImages(files) { progress ->
                _uploadProgress.value = ImageUploadState.Progress(progress)
            }.collect { state ->
                _uploadProgress.value = state

                when (state) {
                    is ImageUploadState.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                uploadMultipleImagesState = UploadMultipleImageState(
                                    isLoading = true,
                                    multipleImagesUrl = emptyList(),
                                    error = null
                                )
                            )
                        }
                    }
                    is ImageUploadState.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                uploadMultipleImagesState = UploadMultipleImageState(
                                    isLoading = false,
                                    error = state.message
                                )
                            )
                        }
                    }
                    is ImageUploadState.MultipleImageSuccess -> {
                        // Update UI state for images upload success
                        _uiState.update { currentState ->
                            currentState.copy(
                                uploadMultipleImagesState = UploadMultipleImageState(
                                    isLoading = false,
                                    success = state.message,
                                    multipleImagesUrl = state.imageUrls.orEmpty(),
                                    error = null
                                )
                            )
                        }
                        // Now that the images are successfully uploaded, add the product with the S3 URLs
                        addProduct(product.copy(images = state.imageUrls.orEmpty()))
                    }
                    else -> { /* no-op */ }
                }
            }
        }
    }

    private fun uploadSingleImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            if (uri == Uri.EMPTY) {
                handleError("No image selected")
                return@launch
            }
            val file = try {
                uriToFile(context, uri)
            } catch (e: IOException) {
                handleError("Failed to process image: ${e.localizedMessage}")
                return@launch
            }

            imageUploadRepository.uploadSingleImage(file) { progress ->
                _uploadProgress.value = ImageUploadState.Progress(progress)
            }.collect { state ->
                _uploadProgress.value = state
                when (state) {
                    is ImageUploadState.Loading -> {
                        _uiState.update {
                            it.copy(
                                uploadSingleImageState = UploadSingleImageUrlState(
                                    isLoading = true,
                                    singleImageUrl = null,
                                    error = null
                                )
                            )
                        }
                    }
                    is ImageUploadState.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                uploadSingleImageState = UploadSingleImageUrlState(
                                    isLoading = false,
                                    success = state.message,
                                    singleImageUrl = null
                                )
                            )
                        }
                    }
                    is ImageUploadState.SingleImageSuccess -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                uploadSingleImageState = UploadSingleImageUrlState(
                                    isLoading = false,
                                    success = state.message,
                                    singleImageUrl = state.imageUrls.orEmpty(),
                                    error = null
                                )
                            )
                        }
                    }
                    else -> { /* no-op */ }
                }
            }
        }
    }

    /**
     * Validates the product input and returns an error message if invalid.
     * Returns null if input is valid.
     */
    private fun validateProductInput(): String? {
        val currentInput = _uiState.value.productInput
        return when {
            currentInput.productName.isEmpty() ||
                    currentInput.productDescription.isEmpty() ||
                    currentInput.productCategory.isEmpty() ||
                    currentInput.productBrand.isEmpty() -> "Please fill in all fields"
            currentInput.productPrice == null || currentInput.productPrice <= 0 ->
                "Please enter a valid price"
            currentInput.productInventory == null || currentInput.productInventory <= 0 ->
                "Please enter a valid inventory count"
            else -> null
        }
    }

    /**
     * Calls the use case to add the product to the database.
     * This function assumes that the [product] already has valid image URLs attached.
     */
    private fun addProduct(product: UploadProductRequest) {
        viewModelScope.launch {
            productUseCases.addProduct.addProduct(product).collect { response ->
                when (response) {
                    is Resource.Loading -> updateLoadingState(true)
                    is Resource.Success -> {
                        updateLoadingState(false)
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                uploadData = response.data?.data,
                                success = "Product has been added successfully",
                                error = null
                            )
                        }
                        _navigateToMyProducts.send(Unit)
                    }
                    is Resource.Error -> {
                        updateLoadingState(false)
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = response.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun handleError(error: String) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = false, error = error)
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Helper function that converts a given URI to a File.
     * Throws an IOException if the file cannot be created.
     */
    private fun uriToFile(context: Context, uri: Uri): File {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return file
        } ?: throw IOException("Failed to open input stream from URI: $uri")
    }
}
