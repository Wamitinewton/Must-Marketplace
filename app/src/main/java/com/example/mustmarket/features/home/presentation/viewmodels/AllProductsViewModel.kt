package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.presentation.event.HomeScreenEvent
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

    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        initializeProducts()
        setUpSearchListener()
    }

    private fun initializeProducts() {
        viewModelScope.launch {
            val currentProducts = productsUiState.value.products
            if (currentProducts.isEmpty()) {
                loadProducts(forceRefresh = true)
            }
        }
    }

    fun onProductEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.Refresh -> loadProducts(forceRefresh = true)
            is HomeScreenEvent.Search -> onSearchQueryChange(event.query)
            is HomeScreenEvent.ClearSearch -> clearSearch()
        }
    }

    @OptIn(FlowPreview::class)
    private fun setUpSearchListener() {
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .filter { query -> query.trim().length >= 2 || query.isEmpty() }
            .onEach { query ->
                when {
                    query.isEmpty() -> loadProducts(forceRefresh = false)
                    else -> performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _viewModelState.update { it.copy(isLoading = true, isSearchActive = true) }
            productsUseCases.homeUseCases.searchProducts(query)
                .collect { result -> handleProductsResult(result) }
        }
    }

    private fun clearSearch() {
        searchJob?.cancel()
        _searchQuery.value = ""
        _viewModelState.update {
            it.copy(searchQuery = "", isSearchActive = false)
        }
        loadProducts(forceRefresh = false)
    }

    private fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _viewModelState.update { it.copy(searchQuery = query, isSearchActive = query.isNotEmpty()) }
    }

    fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            productsUseCases.homeUseCases.getProductsById(productId)
                .collect { result ->
                    _productDetailsState.value = when (result) {
                        is Resource.Success -> result.data?.let {
                            ProductDetailsState.Success(it)
                        } ?: ProductDetailsState.Error("Product not found")

                        is Resource.Error -> ProductDetailsState.Error(
                            result.message ?: "An unexpected error occurred"
                        )

                        is Resource.Loading -> ProductDetailsState.Loading
                    }
                }
        }
    }

    private fun loadProducts(forceRefresh: Boolean) {
        viewModelScope.launch {
            _viewModelState.update { it.copy(isLoading = true) }
            productsUseCases.homeUseCases.getAllProducts(forceRefresh = forceRefresh)
                .collect { result ->
                    handleProductsResult(result)
                    if (forceRefresh) {
                        _viewModelState.update { it.copy(isRefreshing = false) }
                    }
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

                is Resource.Loading -> state.copy(isLoading = result.isLoading)
            }
        }
    }
}

