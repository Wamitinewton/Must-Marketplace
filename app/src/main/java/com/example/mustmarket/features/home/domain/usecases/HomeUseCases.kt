package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.database.entities.BookmarkedProductEntity
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.domain.repository.BookmarkRepository
import com.example.mustmarket.features.home.domain.repository.CategoryRepository
import com.example.mustmarket.features.home.domain.repository.SearchProductsRepository
import java.io.File

class HomeUseCases(
    private val bookmarksRepository: BookmarkRepository,
    private val productRepository: AllProductsRepository,
    private val categoryRepository: CategoryRepository,
    private val searchProductsRepository: SearchProductsRepository
) {
    fun getBookmarkedProducts() = bookmarksRepository.getBookmarkedProducts()

    suspend fun toggleBookmark(product: NetworkProduct) =
        bookmarksRepository.toggleBookmark(product)

    suspend fun removeProduct(product: BookmarkedProductEntity) =
        bookmarksRepository.removeProduct(product)

    suspend fun getAllProducts(forceRefresh: Boolean) =
        productRepository.getAllProducts(forceRefresh = forceRefresh)

    suspend fun shouldRefreshProducts() = productRepository.shouldRefresh()

    suspend fun shouldRefreshCategories() = categoryRepository.shouldRefresh()

    suspend fun getAllCategories(shouldRefresh: Boolean) = categoryRepository.getAllCategories(
        shouldRefresh = shouldRefresh
    )

    suspend fun searchProducts(query: String) = searchProductsRepository.searchProducts(query)

    suspend fun searchBookmarks(query: String) = searchProductsRepository.searchBookmarks(query)

    suspend fun addCategory(image: File, name: String) = categoryRepository.addCategory(image, name)

}

