package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.usecase.UseCases
import com.example.mustmarket.core.coroutineLogger.CoroutineDebugger
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.presentation.event.SearchProductEvent
import com.example.mustmarket.features.home.presentation.state.SearchProductsState
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
import javax.inject.Inject

@HiltViewModel
class SearchProductsViewModel @Inject constructor(
    private val productSearchUseCases: UseCases
) : ViewModel() {
    private val _searchViewModelState = MutableStateFlow(SearchProductsState())
    val searchUiState: StateFlow<SearchProductsState> = _searchViewModelState.asStateFlow()
    private val coroutineDebugger = CoroutineDebugger.getInstance()

    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null


    init {
        setUpSearchListener()
    }


    fun onSearchEvent(event: SearchProductEvent) {
        when(event) {
            SearchProductEvent.ClearSearch -> clearSearch()
            SearchProductEvent.PerformSearch -> performSearch(_searchQuery.value)
            is SearchProductEvent.QueryChanged -> onSearchQueryChange(event.query)
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
                    query.isEmpty() -> clearSearch()
                    else -> performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun clearSearch() {
        searchJob?.cancel()
        _searchQuery.value = ""
        _searchViewModelState.update {
            it.copy(
                searchQuery = "",
                isSearchActive = false,
                searchResults = emptyList(),
                errorMessage = null
            )
        }
    }

    private fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _searchViewModelState.update {
            it.copy(
                searchQuery = query,
                isSearchActive = query.isNotEmpty(),
            )
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = coroutineDebugger.launchTracked(
            scope = viewModelScope,
            tag = "perform_search"
        ) {
            _searchViewModelState.update {
                it.copy(
                    isLoading = true,
                    isSearchActive = true
                )
            }
            productSearchUseCases.homeUseCases.searchProducts(query).collect { result ->
                _searchViewModelState.update { state ->
                    when (result) {
                        is Resource.Success -> state.copy(
                            isLoading = false,
                            searchResults = result.data ?: emptyList(),
                            errorMessage = null
                        )

                        is Resource.Error -> state.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to Search products",
                            searchResults = emptyList()
                        )

                        is Resource.Loading -> state.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

}