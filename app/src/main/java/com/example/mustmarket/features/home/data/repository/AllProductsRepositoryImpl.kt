package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.retryConfig.RetryUtil
import com.example.mustmarket.core.threads.MultiThreadingActivity
import com.example.mustmarket.core.threads.OperationType
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.di.IODispatcher
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import com.example.mustmarket.features.home.data.mapper.toNetworkProducts
import com.example.mustmarket.features.home.data.mapper.toProductListingEntities
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.data.repository.CategoryRepositoryImpl.CacheBatch.BATCH_SIZE
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.workManager.ProductSyncManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AllProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val dao: ProductDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val retryUtil: RetryUtil,
    private val syncManager: ProductSyncManager,
    private val threadingActivity: MultiThreadingActivity
) : AllProductsRepository {


    private val cacheMutex = Mutex()

    private companion object {
        const val NETWORK_TIMEOUT = 30000L // 30 seconds
        const val DB_TIMEOUT = 10000L // 10 seconds
        const val BATCH_SIZE = 50
    }

    override suspend fun shouldRefresh(): Boolean =
        threadingActivity.executeIsolatedThread(
            operationType = OperationType.DATABASE,
            timeOuts = DB_TIMEOUT
        ) {
            dao.getAllProducts().firstOrNull().isNullOrEmpty()
        }.first()


    override suspend fun getAllProducts(forceRefresh: Boolean): Flow<Resource<List<NetworkProduct>>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val cachedProducts = threadingActivity.executeIsolatedThread(
                    operationType = OperationType.DATABASE,
                    timeOuts = DB_TIMEOUT
                ) {
                    dao.getAllProducts().firstOrNull()
                }.first()

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
                val response = threadingActivity.executeIsolatedThread(
                    operationType = OperationType.NETWORK,
                    timeOuts = NETWORK_TIMEOUT
                ) {
                    productsApi.getAllProducts()
                }.first()

                if (response.message == "Success") {
                    val processedProducts = response.data.map { it.toDomainProduct() }
                        .filter { it.brand.isNotBlank() }
                        .sortedByDescending { it.id }
                        .distinctBy { it.id }

                    threadingActivity.executeIsolatedThread(
                        operationType = OperationType.DATABASE,
                        timeOuts = DB_TIMEOUT
                    ) {
                        dao.clearAllProducts()
                        processedProducts.chunked(BATCH_SIZE).forEach { batch ->
                            dao.insertProducts(batch.toProductListingEntities())
                        }
                    }.first()

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
            val response = threadingActivity.executeIsolatedThread(
                operationType = OperationType.NETWORK,
                timeOuts = NETWORK_TIMEOUT
            ) {
                retryUtil.executeWithRetry { productsApi.getProductsById(productId) }
            }.first()

            emit(Resource.Loading(false))

            if (response.message == "Success") {
                emit(Resource.Success(response.data.toDomainProduct()))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(handleError(e))
        }
    }.flowOn(ioDispatcher)

    override suspend fun manageCache() {
        threadingActivity.executeIsolatedThread(
            operationType = OperationType.CACHE,
            timeOuts = DB_TIMEOUT
        ) {
            dao.clearAllProducts()
        }
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