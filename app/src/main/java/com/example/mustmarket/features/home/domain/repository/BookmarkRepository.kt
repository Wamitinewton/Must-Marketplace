package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarkedProducts(): Flow<Resource<List<BookmarkedProduct>>>
    fun isProductBookmarked(productId: Int): Flow<Resource<Boolean>>
    suspend fun toggleBookmark(product: NetworkProduct)
    suspend fun removeProduct(product: BookmarkedProduct)
}