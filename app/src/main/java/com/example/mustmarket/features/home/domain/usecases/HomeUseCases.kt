package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.domain.repository.BookmarkRepository
import com.example.mustmarket.features.home.domain.repository.CategoryRepository

class HomeUseCases(
    private val bookmarksRepository: BookmarkRepository,
    private val productRepository: AllProductsRepository,
    private val categoryRepository: CategoryRepository
) {
    fun getBookmarkedProducts() = bookmarksRepository.getBookmarkedProducts()

    fun isProductBookmarked(productId: Int) = bookmarksRepository.isProductBookmarked(productId)

    suspend fun toggleBookmark(product: NetworkProduct) =
        bookmarksRepository.toggleBookmark(product)

    suspend fun removeProduct(product: BookmarkedProduct) =
        bookmarksRepository.removeProduct(product)

    suspend fun refreshProducts() = productRepository.refreshProducts()

    suspend fun getAllProducts() = productRepository.getAllProducts()

    suspend fun getCategoryBySize(size: Int) = categoryRepository.getCategories(size)

    suspend fun getAllCategories() = categoryRepository.getAllCategories()

    suspend fun refreshCategories() = categoryRepository.refreshCategories()

    suspend fun getProductsById(productId: Int) = productRepository.getProductsById(productId)


}

