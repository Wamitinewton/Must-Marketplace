package com.newton.mustmarket.features.get_products.presentation.state

import com.newton.mustmarket.database.entities.BookmarkedProductEntity

sealed class BookmarksUiState {
    data object Loading: BookmarksUiState()
    data class Success(val bookmarks: List<BookmarkedProductEntity>?): BookmarksUiState()
    data class Error(val message: String?): BookmarksUiState()
}

sealed class BookmarkEvent {
    data class Success(val message: String) : BookmarkEvent()
    data class Error(val message: String?): BookmarkEvent()
}