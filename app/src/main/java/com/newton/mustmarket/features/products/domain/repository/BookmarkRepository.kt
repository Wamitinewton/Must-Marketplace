package com.newton.mustmarket.features.products.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.database.entities.BookmarkedProductEntity
import com.newton.mustmarket.features.products.domain.model.products.NetworkProduct
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarkedProducts(): Flow<Resource<List<BookmarkedProductEntity>>>
    fun isProductBookmarked(productId: Int): Flow<Resource<Boolean>>
    suspend fun toggleBookmark(product: NetworkProduct)
    suspend fun removeProduct(product: BookmarkedProductEntity)
}