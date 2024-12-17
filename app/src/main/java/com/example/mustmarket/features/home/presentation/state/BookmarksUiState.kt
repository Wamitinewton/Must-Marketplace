package com.example.mustmarket.features.home.presentation.state

import com.example.mustmarket.database.entities.BookmarkedProductEntity

sealed class BookmarksUiState {
    data object Loading: BookmarksUiState()
    data class Success(val bookmarks: List<BookmarkedProductEntity>?): BookmarksUiState()
    data class Error(val message: String?): BookmarksUiState()
}

sealed class BookmarkEvent {
    data class Success(val message: String) : BookmarkEvent()
    data class Error(val message: String?): BookmarkEvent()
}