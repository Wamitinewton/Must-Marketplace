package com.example.mustmarket.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.BookmarksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val bookmarksUseCases: UseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow<BookmarksUiState>(BookmarksUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _errorEvent = MutableStateFlow<String?>(null)
    val errorEvent = _errorEvent.asStateFlow()

    private val _successEvent = MutableStateFlow<String?>(null)
    val successEvent = _successEvent.asStateFlow()

    // tuna update flow ya kurefresh cache hapa
    private val _bookmarkStatusUpdates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val bookmarkStatusUpdates = _bookmarkStatusUpdates.asStateFlow()

    init {
        getBookmarkedProducts()
    }

    private fun getBookmarkedProducts() {
        viewModelScope.launch {
            bookmarksUseCases.bookmarks.getBookmarkedProducts()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.value = BookmarksUiState.Success(result.data)

                            _bookmarkStatusUpdates.value = result.data?.associate {
                                it.id to true
                            } ?: emptyMap()
                        }

                        is Resource.Error -> {
                            _uiState.value = BookmarksUiState.Error(result.message)
                            _errorEvent.emit(result.message)
                        }

                        is Resource.Loading -> {
                            _uiState.value = BookmarksUiState.Loading
                        }
                    }
                }
        }
    }

    fun removeBookmark(product: BookmarkedProduct) {
        viewModelScope.launch {
            try {
                bookmarksUseCases.bookmarks.removeProduct(product)
                _bookmarkStatusUpdates.value -= product.id
                _successEvent.emit("Product removed from bookmarks")
                getBookmarkedProducts()
            } catch (e: Exception) {
                _errorEvent.emit(e.localizedMessage ?: "Could not remove bookmark")
            }
        }
    }

    fun toggleBookmarkStatus(product: NetworkProduct) {
        viewModelScope.launch {
            try {
                val isCurrentlyBookmarked = (_uiState.value as? BookmarksUiState.Success)?.bookmarks
                    ?.any { it.id == product.id } ?: false

                bookmarksUseCases.bookmarks.toggleBookmark(product)
                _bookmarkStatusUpdates.value += (product.id to !isCurrentlyBookmarked)

                val successMessage = if (isCurrentlyBookmarked) {
                    "Product removed from bookmarks successfully"
                } else {
                    "Product added to bookmarks successfully"
                }
                _successEvent.emit(successMessage)
                getBookmarkedProducts()
            } catch (e: Exception) {
                _errorEvent.emit(e.localizedMessage ?: "Could not toggle bookmark")
            }
        }
    }
}