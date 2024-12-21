package com.example.mustmarket.features.home.presentation.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.usecase.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import com.example.mustmarket.features.home.presentation.event.CategoryEvent
import com.example.mustmarket.features.home.presentation.state.AddCategoryState
import com.example.mustmarket.features.home.presentation.state.ProductCategoryViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val productUseCases: UseCases
) : ViewModel() {
    private val _viewModelState = MutableStateFlow(ProductCategoryViewModelState())
    val uiState = _viewModelState.asStateFlow()

    init {
        viewModelScope.launch {
            initiateCategoryLoading()
        }
    }

    private suspend fun initiateCategoryLoading() {
        val loadRemote = productUseCases.homeUseCases.shouldRefreshCategories()
        if (loadRemote) {
            getAllCategories(forceRefresh = true)
        } else {
            getAllCategories(forceRefresh = false)
        }
    }

    fun onCategoryEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.Refresh -> getAllCategories(forceRefresh = true)

            is CategoryEvent.CategoryUploadEvent -> {
                if (validateCategory(event.name, event.uri)) {
                    addCategory(event.context, event.name, event.uri)
                }
            }
        }
    }

    private fun addCategory(context: Context, name: String, imageUrl: Uri) {
        viewModelScope.launch {
            _viewModelState.update {
                it.copy(
                    addCategoryState = AddCategoryState(
                        isLoading = true,
                        successMessage = null,
                        errorMessage = null
                    )
                )
            }
            try {
                val imageFile = uriTofFile(context, imageUrl)
                productUseCases.homeUseCases.addCategory(imageFile, name)
                    .collect { response ->
                        when (response) {
                            is Resource.Loading -> updateCategoryLoadingState(true)
                            is Resource.Error -> {
                                _viewModelState.update {
                                    it.copy(
                                        addCategoryState = AddCategoryState(
                                            isLoading = false,
                                            successMessage = null,
                                            errorMessage = response.message
                                                ?: "Failed to add categories"
                                        )
                                    )
                                }
                            }

                            is Resource.Success -> {
                                _viewModelState.update {
                                    it.copy(
                                        addCategoryState = AddCategoryState(
                                            isLoading = false,
                                            successMessage = response.data?.message
                                                ?: "Category added successfully",
                                            errorMessage = null
                                        )
                                    )
                                }
                            }
                        }
                    }
            } catch (e: IOException) {
                handleAddCategoriesError("failed to process category: ${e.localizedMessage}")
            } catch (e: Exception) {
                handleAddCategoriesError("Unexpected error")
            }
        }
    }

    private fun validateCategory(name: String, image: Uri?): Boolean {
        if (name.isBlank()) {
            handleAddCategoriesError("Category name cannot be empty")
            return false
        }
        if (image == null) {
            handleAddCategoriesError("Category Image cannot be null")
            return false
        }
        return true
    }

    private fun updateCategoryLoadingState(isLoading: Boolean) {
        _viewModelState.update {
            it.copy(
                addCategoryState = it.addCategoryState.copy(
                    isLoading = isLoading
                )
            )
        }
    }

    private fun handleAddCategoriesError(error: String) {
        _viewModelState.update {
            it.copy(
                addCategoryState = AddCategoryState(
                    isLoading = false,
                    successMessage = null,
                    errorMessage = error
                )
            )
        }
    }

    private fun getAllCategories(forceRefresh: Boolean) {
        viewModelScope.launch {
            _viewModelState.update { it.copy(isLoading = true) }
            productUseCases.homeUseCases.getAllCategories(shouldRefresh = forceRefresh)
                .collect { result ->
                    handleCategoriesResult(result)
                }
        }
    }


    private fun handleCategoriesResult(result: Resource<List<ProductCategory>>) {
        _viewModelState.update { state ->
            when (result) {
                is Resource.Success -> state.copy(
                    isLoading = false,
                    categories = result.data ?: emptyList(),
                    errorMessage = ""
                )

                is Resource.Error -> state.copy(
                    isLoading = false,
                    errorMessage = result.message ?: "An unexpected error occurred",
                    categories = emptyList()
                )

                is Resource.Loading -> state.copy(
                    isLoading = result.isLoading
                )
            }
        }
    }

    private fun uriTofFile(context: Context, uri: Uri): File {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val file = File(
                context.cacheDir,
                "upload_${System.currentTimeMillis()}.jpg"
            )
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return file
        } ?: throw Exception("Failed to open input stream from URI: $uri")
    }
}