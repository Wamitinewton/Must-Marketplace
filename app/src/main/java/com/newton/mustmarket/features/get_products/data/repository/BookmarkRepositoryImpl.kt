package com.newton.mustmarket.features.get_products.data.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.database.dao.BookmarkDao
import com.newton.mustmarket.database.entities.BookmarkedProductEntity
import com.newton.mustmarket.features.get_products.data.mapper.toBookmarkedProduct
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.get_products.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {
    override  fun getBookmarkedProducts(): Flow<Resource<List<BookmarkedProductEntity>>> = flow {
        emit(Resource.Loading(true))
        try {
            emitAll(bookmarkDao.getBookmarkedProducts().map { products ->
                Resource.Success(products)
            })
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                    data = emptyList()
                )
            )
        }
    }.catch { e ->
        emit(
            Resource.Error(
                message = e.localizedMessage ?: "An unexpected error occurred",
                data = emptyList()
            )
        )
    }

    override  fun isProductBookmarked(productId: Int): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))
        try {
            emitAll(bookmarkDao.isProductBookmarked(productId).map { isBookmarked ->
                Resource.Success(isBookmarked)
            })
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                    data = false
                )
            )
        }
    }.catch { e ->
        emit(
            Resource.Error(
                message = e.localizedMessage ?: "An Unexpected error occurred",
                data = false
            )
        )
    }

    override suspend fun toggleBookmark(product: NetworkProduct) {
        try {
            val bookmarked = bookmarkDao.isProductBookmarked(product.id).first()
            if (bookmarked) {
                bookmarkDao.removeBookmark(product.toBookmarkedProduct())
            } else {
                bookmarkDao.bookmarkProduct(product.toBookmarkedProduct())
            }
        } catch (e: Exception) {
            throw Exception(e.localizedMessage ?: "An error occurred while toggling bookmark")
        }
    }

    override suspend fun removeProduct(product: BookmarkedProductEntity) {
        try {
            bookmarkDao.removeBookmark(product)
        } catch (e: Exception) {
            throw Exception(e.localizedMessage ?: "An error occurred")
        }
    }
}