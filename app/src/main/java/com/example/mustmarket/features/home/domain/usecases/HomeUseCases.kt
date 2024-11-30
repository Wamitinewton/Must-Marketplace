package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
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

    fun isProductBookmarked(productId: Int) = bookmarksRepository.isProductBookmarked(productId)

    suspend fun toggleBookmark(product: NetworkProduct) =
        bookmarksRepository.toggleBookmark(product)

    suspend fun removeProduct(product: BookmarkedProduct) =
        bookmarksRepository.removeProduct(product)

    suspend fun refreshProducts() = productRepository.refreshProducts()

    suspend fun shouldRefresh() = productRepository.shouldRefresh()

    suspend fun getAllProducts(forceRefresh: Boolean) =
        productRepository.getAllProducts(forceRefresh = forceRefresh)

    suspend fun getCategoryBySize(size: Int) = categoryRepository.getCategories(size)

    suspend fun getAllCategories() = categoryRepository.getAllCategories()

    suspend fun refreshCategories() = categoryRepository.refreshCategories()

    suspend fun getProductsById(productId: Int) = productRepository.getProductsById(productId)

    suspend fun searchProducts(query: String) = searchProductsRepository.searchProducts(query)

    suspend fun searchBookmarks(query: String) = searchProductsRepository.searchBookmarks(query)

    suspend fun addCategory(image: File, name: String) = categoryRepository.addCategory(image, name)

}

