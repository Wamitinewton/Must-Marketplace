package com.example.mustmarket.features.home.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.presentation.state.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.state.ProductDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        getAllProducts()
        setUpSearchListener()
    }

    fun onProductEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.Refresh -> refreshProduct()
            is HomeScreenEvent.Search -> onSearchQueryChange(event.query)
            is HomeScreenEvent.ClearSearch -> clearSearch()

        }
    }

    @OptIn(FlowPreview::class)
    private fun setUpSearchListener() {
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .filter { query ->
                query.trim().length >= 2 || query.isEmpty()
            }
            .onEach { query ->
                if (query.isEmpty()) {
                    getAllProducts()
                } else {
                    performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _viewModelState.update { it.copy(isLoading = true) }

            productsUseCases.homeUseCases.searchProducts(query).collect { result ->
                handleProductsResult(result)
            }
        }
    }

    private fun clearSearch() {
        _searchQuery.value = ""
        _viewModelState.update {
            it.copy(
                searchQuery = "",
                isSearchActive = false
            )
        }
        getAllProducts()
    }

    private fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _viewModelState.update { it.copy(searchQuery = query) }
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