package com.newton.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newton.mustmarket.usecase.UseCases
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.database.entities.BookmarkedProductEntity
import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.home.presentation.state.BookmarkEvent
import com.newton.mustmarket.features.home.presentation.state.BookmarksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val bookmarksUseCases: UseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow<BookmarksUiState>(BookmarksUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BookmarkEvent>()
    val events = _events.asSharedFlow()

    // tuna update flow ya kurefresh cache hapa
    private val _bookmarkStatusUpdates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val bookmarkStatusUpdates = _bookmarkStatusUpdates.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        getBookmarkedProducts()
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
                if (query.isEmpty()){
                    getBookmarkedProducts()
                } else {
                    performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun performSearch(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = BookmarksUiState.Loading
            bookmarksUseCases.homeUseCases.searchBookmarks(query).collect { result ->

            }
        }
    }


    private fun getBookmarkedProducts() {
        viewModelScope.launch {
            bookmarksUseCases.homeUseCases.getBookmarkedProducts()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> handleSuccessResult(result.data)

                        is Resource.Error -> handleErrorResult(result.message)

                        is Resource.Loading -> _uiState.value = BookmarksUiState.Loading
                    }
                }
        }
    }

    private fun handleSuccessResult(data: List<BookmarkedProductEntity>?) {
        _uiState.value = BookmarksUiState.Success(data)
        _bookmarkStatusUpdates.value = data?.associate { it.id to true } ?: emptyMap()
    }

    private suspend fun handleErrorResult(message: String?) {
        _uiState.value = BookmarksUiState.Error(message)
        _events.emit(BookmarkEvent.Error(message))
    }

    fun removeBookmark(product: BookmarkedProductEntity) {
        viewModelScope.launch {
            runCatching {
                bookmarksUseCases.homeUseCases.removeProduct(product)
                _bookmarkStatusUpdates.value -= product.id
                _events.emit(BookmarkEvent.Success("Product removed from bookmarks"))
                getBookmarkedProducts()
            }.onFailure { e ->
                _events.emit(BookmarkEvent.Error(e.localizedMessage ?: "Could not remove bookmark"))
            }
        }
    }

    fun toggleBookmarkStatus(product: NetworkProduct) {
        viewModelScope.launch {
            runCatching {
                val isCurrentlyBookmarked = (_uiState.value as? BookmarksUiState.Success)?.bookmarks
                    ?.any { it.id == product.id } ?: false

                bookmarksUseCases.homeUseCases.toggleBookmark(product)
                _bookmarkStatusUpdates.value += (product.id to !isCurrentlyBookmarked)

                val successMessage = if (isCurrentlyBookmarked) {
                    "Product removed from bookmarks"
                } else {
                    "Product added to bookmarks successfully"
                }
                _events.emit(BookmarkEvent.Success(successMessage))
                getBookmarkedProducts()
            }.onFailure { e ->
                _events.emit(BookmarkEvent.Error(e.localizedMessage ?: "Could not toggle bookmark"))
            }
        }
    }
}