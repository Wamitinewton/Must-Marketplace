package com.newton.mustmarket.features.merchant.create_store.presentation.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newton.file_service.domain.model.ImageUploadState
import com.newton.file_service.domain.repository.ImageUploadRepository
import com.newton.file_service.file_config.FileConverter.uriToFile
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest
import com.newton.mustmarket.features.merchant.create_store.merchant_keystore.MerchantPrefsRepository
import com.newton.mustmarket.features.merchant.create_store.presentation.event.MerchantEvent
import com.newton.mustmarket.features.merchant.create_store.presentation.state.CreateMerchantState
import com.newton.mustmarket.features.merchant.create_store.presentation.state.UploadBannerAndProfileState
import com.newton.mustmarket.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    private val merchantUseCase: UseCases,
    private val imageUploadRepository: ImageUploadRepository,
    private val merchantPrefsRepository: MerchantPrefsRepository
): ViewModel() {

    private val _isMerchant = MutableStateFlow(false)
    val isMerchant = _isMerchant.asStateFlow()

    private val _createMerchantState = MutableStateFlow(CreateMerchantState())
    val createMerchantState = _createMerchantState.asStateFlow()

    private val _uploadProgress = MutableStateFlow<ImageUploadState>(ImageUploadState.Initial)
    val uploadProgress = _uploadProgress.asStateFlow()

    private val _navigateToMyStore = Channel<Unit>(Channel.CONFLATED)
    val navigateToMyStore = _navigateToMyStore.receiveAsFlow()

    private var currentUploadJob: Job? = null

    init {
        viewModelScope.launch {
            merchantPrefsRepository.getMerchantStatus()
                .collect { status ->
                    _isMerchant.value = status
                }
        }
    }


    private fun updateMerchantStatus(isSuccess: Boolean) {
        viewModelScope.launch {
            if (isSuccess) {
                Timber.d("Setting merchant to true")
                merchantPrefsRepository.setMerchantStatus(true)
                Timber.d("Merchant state is set")
            }
        }
    }

    fun handleEvent(event: MerchantEvent) {
        when(event) {
            MerchantEvent.ClearError -> clearError()
            is MerchantEvent.ShopDescriptionChanged -> {
                _createMerchantState.update { currentState ->
                    currentState.copy(
                        merchantDetailsInput = currentState.merchantDetailsInput.copy(
                            shopDescription = event.description
                        )
                    )
                }
            }
            is MerchantEvent.MerchantNameChanged -> {
                _createMerchantState.update { currentState ->
                    currentState.copy(
                        merchantDetailsInput = currentState.merchantDetailsInput.copy(
                            merchantName = event.name
                        )
                    )
                }
            }
            is MerchantEvent.BannerAndProfileUpload -> handleImageUpload(event)
            is MerchantEvent.ShopLocationChanged -> {
                _createMerchantState.update { currentState ->
                    currentState.copy(
                        merchantDetailsInput = currentState.merchantDetailsInput.copy(
                            location = event.location
                        )
                    )
                }
            }
        }
    }

    private fun handleImageUpload(event: MerchantEvent.BannerAndProfileUpload) {
        currentUploadJob?.cancel()
        currentUploadJob = viewModelScope.launch {
            uploadMerchant(
                event.context,
                event.bannerUri,
                event.profileUri,
                event.createMerchantRequest
            )
        }
    }

    private fun uploadMerchant(
        context: Context,
        bannerUri: Uri?,
        profileUri: Uri?,
        merchantRequest: CreateMerchantRequest
    ) {
            validateMerchantInput()?.let { errorMsg ->
                _createMerchantState.update { it.copy(isLoading = false, error = errorMsg) }
                return
            }

            if (bannerUri == null || profileUri == null) {
                _createMerchantState.update {
                    it.copy(isLoading = false, error = "Banner or Profile URI is missing.")
                }
                return
            }


            val files = try {
                listOf(
                    uriToFile(context, bannerUri),
                    uriToFile(context, profileUri)
                )
            }catch (e: Exception) {
                _createMerchantState.update { it.copy(isLoading = false, error = "Failed to process images: ${e.localizedMessage}") }
                return
            }
            _createMerchantState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            imageUploadRepository.uploadMultipleImages(files) { progress ->
                _uploadProgress.value = ImageUploadState.Progress(progress)
            }.collect { state ->
                when(state) {
                    is ImageUploadState.Error -> {
                        _createMerchantState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                uploadBannerAndProfileState = UploadBannerAndProfileState(
                                    isLoading = false,
                                    error = state.message
                                )
                            )
                        }
                    }
                    ImageUploadState.Loading -> {
                        _createMerchantState.update { currentState ->
                            currentState.copy(
                                uploadBannerAndProfileState = UploadBannerAndProfileState(
                                    isLoading = true,
                                    multipleImagesUrl = emptyList(),
                                    error = null
                                )
                            )
                        }
                    }
                    is ImageUploadState.MultipleImageSuccess -> {
                        val imageUrls = state.imageUrls.orEmpty()
                        if (imageUrls.size != 2) {
                            _createMerchantState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Image upload failed: Expected 2 images, got ${imageUrls.size}"
                                )
                            }
                            return@collect
                        }
                        _createMerchantState.update { currentState ->
                            currentState.copy(
                                uploadBannerAndProfileState = UploadBannerAndProfileState(
                                    isLoading = false,
                                    success = state.message,
                                    multipleImagesUrl = imageUrls,
                                    error = null
                                )
                            )
                        }
                        createMerchant(merchantRequest.copy(
                            banner = imageUrls[0],
                            profile = imageUrls[1],
                        ))
                        updateMerchantStatus(true)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun createMerchant(merchantRequest: CreateMerchantRequest) {
        viewModelScope.launch {
            merchantUseCase.merchantUseCase.addMerchant(merchantRequest).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        updateLoadingState(false)
                        _createMerchantState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = response.message
                            )
                        }
                    }
                    is Resource.Loading -> updateLoadingState(true)
                    is Resource.Success -> {
                        _createMerchantState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                responseData = response.data?.data,
                                success = "Congratulations. You are now a merchant",
                                error = null
                            )
                        }
                        _navigateToMyStore.trySend(Unit)
                    }
                }
            }
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _createMerchantState.update {
            it.copy(isLoading = isLoading)
        }
    }


    private fun clearError() {
        _createMerchantState.update { it.copy(error = null) }
    }

    private fun validateMerchantInput(): String? {
        val currentInput = _createMerchantState.value.merchantDetailsInput
        return when {
            currentInput.merchantName.isEmpty() ||
                    currentInput.shopDescription.isEmpty() ||
                    currentInput.location.isEmpty() -> "Please fill in all fields"

            else -> null
        }
    }

}