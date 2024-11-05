package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.ProductCategory
import com.example.mustmarket.features.home.domain.usecases.HomeUseCases
import com.example.mustmarket.features.home.presentation.state.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.state.ProductCategoryViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val productUseCases: UseCases
) : ViewModel() {
    private val _viewModelState = MutableStateFlow(ProductCategoryViewModelState())
    val uiState = _viewModelState.asStateFlow()

    init {
        getAllCategories()
    }

    fun onCategoryEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.Refresh -> {
                refreshCategories()
            }

            is HomeScreenEvent.ClearSearch -> TODO()
            is HomeScreenEvent.Search -> TODO()
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            productUseCases.homeUseCases.getAllCategories()
                .collect { result ->
                    if (result is Resource.Success && result.data.isNullOrEmpty()) {
                        refreshCategories()
                    } else {
                        handleCategoriesResult(result)
                    }
                }
        }
    }

    private fun refreshCategories() {
        viewModelScope.launch {
            productUseCases.homeUseCases.refreshCategories()
                .collect { result ->
                    handleCategoriesResult(result)
                }
        }
    }

    fun getCategoriesWithLimit(size: Int) {
        viewModelScope.launch {
            productUseCases.homeUseCases.getCategoryBySize(size)
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
                    isLoading = true
                )
            }
        }
    }
}