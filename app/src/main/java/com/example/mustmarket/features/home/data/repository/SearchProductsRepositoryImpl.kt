package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.data.local.db.BookmarkDao
import com.example.mustmarket.features.home.data.local.db.ProductDao
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import com.example.mustmarket.features.home.data.mapper.toNetworkProduct
import com.example.mustmarket.features.home.data.mapper.toProductListingEntity
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.SearchProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SearchProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val dao: ProductDao,
    private val bookmarkDao: BookmarkDao
) : SearchProductsRepository {
    override suspend fun searchProducts(query: String): Flow<Resource<List<NetworkProduct>>> =
        flow {
            emit(Resource.Loading(true))

            try {
                val localResults = dao.searchProducts(query)
                if (localResults.isNotEmpty()) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(localResults.map { it.toNetworkProduct() }))
                    return@flow
                }
            } catch (e: Exception) {
                emit(Resource.Error("Local search failed: ${e.message}"))
            }

            try {
                val response = productsApi.searchProducts(query)


                if (response.message == "Success") {
                    val products = response.data
                        .filter { it.brand.isNotBlank() }
                        .map { it.toDomainProduct() }
                        .sortedByDescending { it.id }
                    emit(Resource.Loading(false))
                    if (products.isEmpty()) {
                        emit(Resource.Error("No products found for '$query'"))
                    } else {
                        try {
                            dao.insertProducts(products.map { it.toProductListingEntity() })
                        } catch (e: Exception) {
                            emit(Resource.Error("Failed to cache search results: ${e.message}"))
                        }

                        emit(Resource.Success(products))
                    }
                } else {
                    emit(Resource.Error(response.message))
                }
            } catch (e: HttpException) {
                emit(Resource.Error("Network error: ${e.message}"))
            } catch (e: IOException) {
                emit(Resource.Error("IO error: ${e.message}"))
            } catch (e: Exception) {
                emit(Resource.Error("Unknown error: ${e.message}"))
            }
        }

    override suspend fun searchBookmarks(query: String): Flow<Resource<List<NetworkProduct>>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val bookmarkResults = bookmarkDao.searchBookmarks(query)
                if (bookmarkResults.isNotEmpty()) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(bookmarkResults.map { it.toNetworkProduct() }))
                    return@flow
                }
            } catch (e: Exception) {
                emit(Resource.Error("Local search failed: ${e.message}"))
            }
        }
}