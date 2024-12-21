package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.presentation.event.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.usecase.UseCases
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


    init {
        viewModelScope.launch {
            initiateProductLoading()
        }
    }

    private suspend fun initiateProductLoading() {
        val loadRemote = productsUseCases.homeUseCases.shouldRefreshProducts()
        if (loadRemote) {
            loadProducts(forceRefresh = true)
        } else {
            loadProducts(forceRefresh = false)
        }
    }

    fun onProductEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.Refresh -> loadProducts(forceRefresh = true)
        }
    }

    private fun loadProducts(forceRefresh: Boolean) {
        viewModelScope.launch {
            _viewModelState.update { it.copy(isLoading = true) }
            productsUseCases.homeUseCases.getAllProducts(forceRefresh = forceRefresh)
                .collect { result ->
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
                    errorMessage = null
                )

                is Resource.Error -> state.copy(
                    isLoading = false,
                    errorMessage = result.message ?: "An unexpected error occurred",
                    products = emptyList()
                )

                is Resource.Loading -> state.copy(isLoading = result.isLoading)
            }
        }
    }
}

