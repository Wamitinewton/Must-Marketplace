package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.presentation.event.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.workManager.ProductSyncManager
import com.example.mustmarket.features.home.workManager.ProductSyncState
import com.example.mustmarket.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val productsUseCases: UseCases,
    syncManager: ProductSyncManager
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )


    private val _viewModelState = MutableStateFlow(AllProductsViewModelState())
    val productsUiState: StateFlow<AllProductsViewModelState> = _viewModelState.asStateFlow()

    private val syncState: StateFlow<ProductSyncState> = syncManager.getLastSyncState()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ProductSyncState()
        )

    init {
        viewModelScope.launch {
//            initiateProductLoading()
            merge(
                syncState
                    .filter { it.isSyncing }
                    .map { true },
                refreshTrigger.map { true }
            )
                .distinctUntilChanged()
                .onStart { emit(true) }
                .flatMapLatest { shouldRefresh ->
                    productsUseCases.homeUseCases.getAllProducts(shouldRefresh)
                        .onStart {
                            if (!syncState.value.isSyncing) {
                                emit(Resource.Loading(true))
                            }
                        }
                        .onCompletion {
                            if (!syncState.value.isSyncing) {
                                emit(Resource.Loading(false))
                            }
                        }
                }
                .catch { error ->
                    emit(Resource.Error(error.message ?: "An unexpected error occurred"))
                }
                .collect { result ->
                    handleProductsResult(result)
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

