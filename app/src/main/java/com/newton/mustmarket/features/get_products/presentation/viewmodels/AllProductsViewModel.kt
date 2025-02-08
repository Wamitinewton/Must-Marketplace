package com.newton.mustmarket.features.get_products.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.core.util.RetryableViewModel
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.get_products.presentation.event.HomeScreenEvent
import com.newton.mustmarket.features.get_products.presentation.state.AllProductsViewModelState
import com.newton.mustmarket.features.get_products.presentation.state.GetProductsByCategoryState
import com.newton.mustmarket.usecase.UseCases
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
) : RetryableViewModel() {


    private val _viewModelState = MutableStateFlow(AllProductsViewModelState())
    val productsUiState: StateFlow<AllProductsViewModelState> = _viewModelState.asStateFlow()

    private val _getProductByCategoryState = MutableStateFlow(GetProductsByCategoryState())
    val getProductsByCategoryState: StateFlow<GetProductsByCategoryState> = _getProductByCategoryState.asStateFlow()

    private var currentCategory: String? = null




    init {
        viewModelScope.launch {
            initiateProductLoading()
            retryTrigger.collect {
                if (currentCategory != null) {
                    getProductsByCategory(currentCategory!!)
                }
            }
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
            is HomeScreenEvent.OnCategoryClicked -> getProductsByCategory(event.category)
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

    private fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            _getProductByCategoryState.update { it.copy(isLoading = true) }
            productsUseCases.homeUseCases.getProductsByCategory(category)
                .collect { result ->
                    handleProductByCategoryResult(result)
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

    private fun handleProductByCategoryResult(result: Resource<List<NetworkProduct>>) {
        _getProductByCategoryState.update { state ->
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

    override fun handleRetry() {
        currentCategory?.let { category ->
            getProductsByCategory(category)
        }
    }

}

