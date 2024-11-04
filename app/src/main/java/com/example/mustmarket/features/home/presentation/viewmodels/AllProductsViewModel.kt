package com.example.mustmarket.features.home.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.presentation.state.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.state.ProductDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val productsUseCases: UseCases,
) : ViewModel() {
    private val _viewModelState = MutableStateFlow(AllProductsViewModelState())
    val productsUiState: StateFlow<AllProductsViewModelState> = _viewModelState.asStateFlow()

    private val _productDetailsState =
        MutableStateFlow<ProductDetailsState>(ProductDetailsState.Loading)
    val productDetailsState: StateFlow<ProductDetailsState> = _productDetailsState.asStateFlow()

    private var currentProductId: Int? = null

    init {
        getAllProducts()
    }

    fun onProductEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.Refresh -> {
                refreshProduct()
            }

        }
    }

    fun loadProductDetails(productId: Int) {
        currentProductId = productId
        viewModelScope.launch {

            Log.d("ProductDetails", "Loading product details for ID: $productId")

            productsUseCases.homeUseCases.getProductsById(productId).collect { result ->
                Log.d("ProductDetails", "Received result: $result")
                when (result) {
                    is Resource.Loading -> {
                        _productDetailsState.value = ProductDetailsState.Loading
                    }

                    is Resource.Success -> {
                        result.data?.let { product ->
                            _productDetailsState.value = ProductDetailsState.Success(product)
                        }
                    }

                    is Resource.Error -> {
                        _productDetailsState.value = ProductDetailsState.Error(
                            result.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            productsUseCases.homeUseCases.getAllProducts().collect { result ->
                if (result is Resource.Success && result.data.isNullOrEmpty()) {
                    refreshProduct()
                } else {
                    handleProductsResult(result)
                }
            }
        }
    }

    private fun refreshProduct() {
        viewModelScope.launch {
            productsUseCases.homeUseCases.refreshProducts().collect { result ->
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