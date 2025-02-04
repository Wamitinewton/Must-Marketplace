package com.newton.mustmarket.features.merchant.products.presentation.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newton.mustmarket.core.coroutineLogger.CoroutineDebugger
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
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UploadProductViewModel @Inject constructor(
    private val productUseCases: UseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(UploadProductUiState())
    val uiState: StateFlow<UploadProductUiState> = _uiState.asStateFlow()

    private val coroutineDebugger = CoroutineDebugger.getInstance()

    private val _navigateToMyProducts = Channel<Unit>()
    val navigateToMyProducts = _navigateToMyProducts.receiveAsFlow()

    override fun onCleared() {
        super.onCleared()
        coroutineDebugger.cancelAllCoroutines()
        val activeCoroutines = coroutineDebugger.getActiveCoroutinesInfo()
        if (activeCoroutines.isNotEmpty()) {
            Timber.tag("register")
                .d("⚠️ Warning: ${activeCoroutines.size} coroutines were still active when ViewModel was cleared:")
            activeCoroutines.forEach { info ->
                Timber.tag("register")
                    .d("📌 Coroutine ${info.id} (${info.tag}) - Running for ${info.duration}ms")
            }
        }
    }

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
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "Product_Brand"
                ) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productInput = currentState.productInput.copy(
                                productBrand = event.brand
                            )
                        )
                    }
                }
            }

            is UploadEvent.ProductCategoryChanged -> {
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "Product_Category"
                ) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productInput = currentState.productInput.copy(
                                productCategory = event.category
                            )
                        )
                    }
                }
            }

            is UploadEvent.ProductDescriptionChanged -> {
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "Product_Description"
                ) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productInput = currentState.productInput.copy(
                                productDescription = event.description
                            )
                        )
                    }
                }
            }

            is UploadEvent.ProductInventoryChanged -> {
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "Product_Inventory"
                ) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productInput = currentState.productInput.copy(
                                productInventory = event.inventory
                            )
                        )
                    }
                }
            }

            is UploadEvent.ProductNameChanged -> {
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "Product_Name"
                ) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productInput = currentState.productInput.copy(
                                productName = event.name
                            )
                        )
                    }
                }
            }

            is UploadEvent.ProductPriceChanged -> {
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "Product_Price"
                ) {
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
    }


    private fun uploadSingleImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                if (uri == Uri.EMPTY) {
                    handleError("No image selected")
                    return@launch
                }
                val file = uriToFile(context, uri)
                productUseCases.addProduct.uploadSingleImages(file)
                    .collect { response ->
                        when (response) {
                            is Resource.Error -> {
                                handleError(response.message ?: "Failed to upload image")
                                updateLoadingState(false)
                            }

                            is Resource.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        uploadSingleImageState = UploadSingleImageUrlState(
                                            isLoading = true,
                                            singleImageUrl = "",
                                            error = null
                                        )
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        uploadSingleImageState = UploadSingleImageUrlState(
                                            isLoading = false,
                                            success = "${response.data?.message}. Image uploaded successfully",
                                            singleImageUrl = response.data?.data.orEmpty(),
                                            error = null
                                        )

                                    )
                                }
                            }
                        }
                    }
            } catch (e: IOException) {
                handleError("Failed to process image: ${e.localizedMessage}")
                updateLoadingState(false)
            }
        }
    }

    private fun uploadImageList(context: Context, uri: List<Uri>, product: UploadProductRequest) {
        val productName = _uiState.value.productInput.productName
        val productDescription = _uiState.value.productInput.productDescription
        val productPrice = _uiState.value.productInput.productPrice
        val productInventory = _uiState.value.productInput.productInventory
        val productCategory = _uiState.value.productInput.productCategory
        val productBrand = _uiState.value.productInput.productBrand
        coroutineDebugger.launchTracked(
            scope = viewModelScope,
            tag = "Upload_Images"
        ) {
            if (uri.isEmpty()) {
                handleError("No images selected")
                return@launchTracked
            }
            when {
                productName.isEmpty() || productDescription.isEmpty() || productCategory.isEmpty() || productBrand.isEmpty() -> {
                    handleError("Please fill in all fields")
                    return@launchTracked
                }

                productPrice == null || productPrice <= 0 -> {
                    handleError("Please enter a valid price")
                    return@launchTracked
                }

                productInventory == null || productInventory <= 0 -> {
                    handleError("Please enter a valid inventory count")
                    return@launchTracked
                }
            }
            try {
                val files = uri.map { uriToFile(context, it) }
                productUseCases.addProduct.uploadListOfImages(files)
                    .collect { response ->
                        when (response) {
                            is Resource.Error -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        isLoading = false,
                                        uploadMultipleImagesState = UploadMultipleImageState(
                                            isLoading = false,
                                            multipleImagesUrl = emptyList(),
                                            error = response.message
                                        )
                                    )
                                }
                            }

                            is Resource.Loading -> {
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

                            is Resource.Success -> {
                                val uploadedUrls = response.data?.data.orEmpty()

                                _uiState.update { currentState ->
                                    currentState.copy(
                                        uploadMultipleImagesState = UploadMultipleImageState(
                                            isLoading = false,
                                            success = "${response.data?.message}. Images uploaded successfully",
                                            multipleImagesUrl = uploadedUrls,
                                            error = null
                                        )
                                    )
                                }
                                addProduct(product.copy(images = uploadedUrls))
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

        coroutineDebugger.launchTracked(
            scope = viewModelScope,
            tag = "Add_Product"
        ) {
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
                                    success = "Product has been added successfully",
                                    error = null
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = response.message,
                                uploadMultipleImagesState = UploadMultipleImageState(
                                    isLoading = false,
                                )
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