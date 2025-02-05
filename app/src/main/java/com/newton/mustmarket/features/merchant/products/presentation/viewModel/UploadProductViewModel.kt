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

    private val _uploadProgress = MutableStateFlow<ImageUploadState>(ImageUploadState.Initial)
    val uploadProgress = _uploadProgress.asStateFlow()

    private val _navigateToMyProducts = Channel<Unit>()
    val navigateToMyProducts = _navigateToMyProducts.receiveAsFlow()


    fun handleEvent(event: UploadEvent) {
        when (event) {
            UploadEvent.ClearError -> clearError()
            is UploadEvent.MultipleImagesUpload -> uploadImageList(
                event.context,
                event.uris,
                event.product
            )

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

    private fun uploadSingleImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                if (uri == Uri.EMPTY) {
                    handleError("No image Selected")
                    return@launch
                }
                val file = uriToFile(context, uri)

                imageUploadRepository.uploadSingleImage(file) { progress ->
                    _uploadProgress.value = ImageUploadState.Progress(progress)
                }.collect { state ->
                    _uploadProgress.value = state
                    when (state) {
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
                        ImageUploadState.Loading -> {
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
                        else -> {}
                    }
                }
            } catch (e: IOException) {
                _uploadProgress.value = ImageUploadState.Error("Failed to process image: ${e.localizedMessage}")
            }
        }
    }

    private fun uploadImageList(context: Context, uris: List<Uri>, product: UploadProductRequest) {
        viewModelScope.launch {
            validateProductInput()?.let {
                _uploadProgress.value = ImageUploadState.Error(it)
                return@launch
            }

            if (uris.isEmpty()) {
                _uploadProgress.value = ImageUploadState.Error("No images selected")
                return@launch
            }

            try {
                val files = uris.map { uriToFile(context, it) }

                imageUploadRepository.uploadMultipleImages(files) { progress ->
                    _uploadProgress.value = ImageUploadState.Progress(progress)
                }.collect { state ->
                    _uploadProgress.value = state

                    when(state) {
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
                        ImageUploadState.Loading -> {
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
                        is ImageUploadState.MultipleImageSuccess -> {
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
                            addProduct(product.copy(images = state.imageUrls.orEmpty()))
                        }
                        else -> {}
                    }
                }
            } catch (e: IOException) {
                _uploadProgress.value = ImageUploadState.Error("Failed to process images: ${e.localizedMessage}")
            }
        }
    }


    private fun validateProductInput(): String? {
        val currentState = _uiState.value.productInput
        return when {
            currentState.productName.isEmpty() ||
                    currentState.productDescription.isEmpty() ||
                    currentState.productCategory.isEmpty() ||
                    currentState.productBrand.isEmpty() -> "Please fill in all fields"

            currentState.productPrice == null ||
                    currentState.productPrice <= 0 -> "Please enter a valid price"

            currentState.productInventory == null ||
                    currentState.productInventory <= 0 -> "Please enter a valid inventory count"

            else -> null
        }
    }


    private fun addProduct(product: UploadProductRequest) {

        viewModelScope.launch {
            productUseCases.addProduct.addProduct(product).collect { response ->
                when (response) {
                    is Resource.Loading -> updateLoadingState(true)
                    is Resource.Success -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    uploadData = response.data?.data,
                                    success = "Product has been added successfully",
                                    error = null
                                )
                            }
                        }


                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = response.message,
                            )
                        }
                    }
                }
            }
        }
    }


    private fun updateLoadingState(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading,
            )
        }
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