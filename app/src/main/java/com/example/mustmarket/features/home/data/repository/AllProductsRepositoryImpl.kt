package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.di.IODispatcher
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import com.example.mustmarket.features.home.data.mapper.toNetworkProducts
import com.example.mustmarket.features.home.data.mapper.toProductListingEntities
import com.example.mustmarket.features.home.data.remote.api_service.ProductsApi
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.workManager.ProductSyncManager
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
    private val syncManager: ProductSyncManager,
) : AllProductsRepository {


    private companion object {
        const val BATCH_SIZE = 50
    }

    override suspend fun shouldRefresh(): Boolean =
        dao.getAllProducts().firstOrNull().isNullOrEmpty()



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

    private suspend fun fetchAndProcessProducts(): Resource<List<NetworkProduct>> =

        withContext(ioDispatcher) {
            try {
               val response = productsApi.getAllProducts()

                if (response.message == "Success") {
                    val processedProducts = response.data.map { it.toDomainProduct() }
                        .filter { it.brand.isNotBlank() }
                        .sortedByDescending { it.id }
                        .distinctBy { it.id }

                    dao.clearAllProducts()
                    processedProducts.chunked(BATCH_SIZE).forEach { batch ->
                        dao.insertProducts(batch.toProductListingEntities())
                    }

                    syncManager.updateLastSyncTimeStamp()

                    val freshProducts = dao.getAllProducts().firstOrNull()
                    if (!freshProducts.isNullOrEmpty()) {
                        Resource.Success(freshProducts.toNetworkProducts())
                    } else {
                        Resource.Error("No products found")
                    }
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