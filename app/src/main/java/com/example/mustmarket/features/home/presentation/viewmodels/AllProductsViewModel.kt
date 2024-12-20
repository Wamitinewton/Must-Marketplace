package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.presentation.event.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.presentation.state.ProductDetailsState
import com.example.mustmarket.usecase.UseCases
=======
import com.example.mustmarket.usecase.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.data.datastore.UserData
import com.example.mustmarket.features.auth.data.datastore.UserStoreManager
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.presentation.event.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.state.ProductDetailsState
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val productsUseCases: UseCases,
<<<<<<< HEAD
) : ViewModel() {

=======
    private val userStoreManager: UserStoreManager
) : ViewModel() {
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

    private val _viewModelState = MutableStateFlow(AllProductsViewModelState())
    val productsUiState: StateFlow<AllProductsViewModelState> = _viewModelState.
        onStart {
            loadProducts(forceRefresh = true)
        }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AllProductsViewModelState()
    )

    private val _productDetailsState =
        MutableStateFlow<ProductDetailsState>(ProductDetailsState.Loading)
    val productDetailsState: StateFlow<ProductDetailsState> = _productDetailsState.asStateFlow()


    init {
<<<<<<< HEAD
//        initializeProducts()
    }


=======
        observeUserData()
//        initializeProducts()
    }

    private fun observeUserData() {
        viewModelScope.launch {
            _userData.value = userStoreManager.fetchUserData()

            while (true) {
                kotlinx.coroutines.delay(1000)
                val currentData = _userData.value
                val newData = userStoreManager.fetchUserData()

                if (newData != currentData) {
                    _userData.value = newData
                }
            }
        }
    }
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

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
        }
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

