package com.newton.mustmarket.features.get_products.data.repository

import coil.network.HttpException
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.database.dao.ProductDao
import com.newton.mustmarket.di.IODispatcher
import com.newton.mustmarket.features.get_products.data.mapper.toDomainProduct
import com.newton.mustmarket.features.get_products.data.mapper.toNetworkProducts
import com.newton.mustmarket.features.get_products.data.mapper.toProductListingEntities
import com.newton.mustmarket.features.get_products.data.remote.api_service.ProductsApi
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.get_products.domain.repository.AllProductsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AllProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val dao: ProductDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AllProductsRepository {


    private companion object {
        const val BATCH_SIZE = 50
    }



    override suspend fun shouldRefresh(): Boolean =
        withContext(ioDispatcher) {
            dao.getAllProducts().firstOrNull().isNullOrEmpty()
        }



    override suspend fun getAllProducts(forceRefresh: Boolean): Flow<Resource<List<NetworkProduct>>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val cachedProducts = withContext(ioDispatcher){
                    dao.getAllProducts().firstOrNull()
                }
                if (!forceRefresh && !cachedProducts.isNullOrEmpty()) {
                    emit(Resource.Success(cachedProducts.toNetworkProducts()))
                } else {
                    val fetchResult = fetchAndProcessProducts()
                    emit(fetchResult)
                }

            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error occurred"))
            } finally {
                emit(Resource.Loading(false))
            }
        }.flowOn(ioDispatcher)

    override suspend fun getProductByCategory(category: String): Flow<Resource<List<NetworkProduct>>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = productsApi.getProductsByCategory(category)

            if (response.message == "Success") {
                val products = response.data.map { it.toDomainProduct() }
                emit(Resource.Success(data = products))
            } else {
                emit(Resource.Error(response.message ?: "Unknown error"))
            }

        } catch (e: HttpException) {
            val errorMessage = when (e.response.code) {
                404 -> "No products found in this category"
                500 -> "Server error, please try again later"
                else -> "Unexpected error: ${e.message}"
            }
            emit(Resource.Error(errorMessage))
        } catch (e: IOException) {
            emit(Resource.Error("Network error, check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error("No products found for this category"))
        }
    }


    private suspend fun fetchAndProcessProducts(): Resource<List<NetworkProduct>> =

        withContext(ioDispatcher) {
            try {
               val response = productsApi.getAllProducts()

                if (response.message == "Success") {
                    val processedProducts = response.data
                        .asSequence()
                        .map { it.toDomainProduct() }
                        .filter { it.brand.isNotBlank() }
                        .sortedByDescending { it.id }
                        .distinctBy { it.id }
                        .toList()

                    dao.run {
                        dao.clearAllProducts()
                        processedProducts.chunked(BATCH_SIZE).forEach{ batch ->
                            dao.insertProducts(batch.toProductListingEntities())
                        }
                    }

                    dao.clearAllProducts()
                    processedProducts.chunked(BATCH_SIZE).forEach { batch ->
                        dao.insertProducts(batch.toProductListingEntities())
                    }


                  Resource.Success(processedProducts)
                } else {
                    Resource.Error(response.message)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }

    override suspend fun getProductsById(productId: Int): Flow<Resource<NetworkProduct>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = withContext(ioDispatcher) {
                productsApi.getProductsById(productId)
            }

            emit(Resource.Loading(false))

            if (response.message == "Success") {
                emit(Resource.Success(response.data.toDomainProduct()))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(handleError(e))
        }
    }

    override suspend fun clearCache() {
        dao.clearAllProducts()
    }


    private fun <T> handleError(exception: Throwable): Resource<T> {
        return when (exception) {
            is HttpException -> Resource.Error(
                "Network error: ${exception.message}"
            )

            is IOException -> Resource.Error(
                "IO error: ${exception.message ?: "Unknown IO error"}"
            )

            else -> Resource.Error(
                exception.message ?: "Unknown error occurred"
            )
        }
    }


}