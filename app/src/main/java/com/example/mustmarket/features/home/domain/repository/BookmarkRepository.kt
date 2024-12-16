package com.example.mustmarket.features.home.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.database.entities.BookmarkedProductEntity
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarkedProducts(): Flow<Resource<List<BookmarkedProductEntity>>>
    fun isProductBookmarked(productId: Int): Flow<Resource<Boolean>>
    suspend fun toggleBookmark(product: NetworkProduct)
    suspend fun removeProduct(product: BookmarkedProductEntity)
}