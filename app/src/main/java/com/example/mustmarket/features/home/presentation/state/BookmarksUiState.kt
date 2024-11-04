package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct

sealed class BookmarksUiState {
    data object Loading: BookmarksUiState()
    data class Success(val bookmarks: List<BookmarkedProduct>?): BookmarksUiState()
    data class Error(val message: String?): BookmarksUiState()
}

sealed class BookmarkEvent {
    data class Success(val message: String) : BookmarkEvent()
    data class Error(val message: String?): BookmarkEvent()
}