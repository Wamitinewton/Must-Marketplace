package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.BookmarkRepository

class Bookmarks(
    private val repository: BookmarkRepository
) {
    fun getBookmarkedProducts() = repository.getBookmarkedProducts()

    fun isProductBookmarked(productId: Int) = repository.isProductBookmarked(productId)

    suspend fun toggleBookmark(product: NetworkProduct) = repository.toggleBookmark(product)

    suspend fun removeProduct(product: BookmarkedProduct) = repository.removeProduct(product)
}
