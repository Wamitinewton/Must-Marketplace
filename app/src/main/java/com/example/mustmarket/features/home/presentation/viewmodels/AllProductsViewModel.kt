package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.presentation.state.HomeScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val productsUseCases: UseCases,
) : ViewModel() {
    private val _viewModelState = MutableStateFlow(AllProductsViewModelState())
    val productsUiState = _viewModelState.asStateFlow()

    init {
        getAllProducts()
    }

    fun onProductEvent(event: HomeScreenEvent){
        when(event){
            is HomeScreenEvent.Refresh -> {
                refreshProduct()
            }
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            productsUseCases.allProducts().collect { result ->
                handleProductsResult(result)
            }
        }
    }

    private fun refreshProduct() {
        viewModelScope.launch {
            productsUseCases.refreshProduct().collect { result ->
                handleProductsResult(result)
            }
        }
    }

    private fun handleProductsResult(result: Resource<List<NetworkProduct>>) {
        _viewModelState.update { state ->
            when (result) {
                is Resource.Success -> state.copy(
                    isLoading = false,
                    products = result.data ?: emptyList(),
                    errorMessage = ""
                )

                is Resource.Error -> state.copy(
                    isLoading = false,
                    errorMessage = result.message ?: "An unexpected error occurred",
                    products = emptyList()
                )

                is Resource.Loading -> state.copy(
                    isLoading = true,
                    products = emptyList(),
                    errorMessage = ""
                )
            }
        }
    }
}