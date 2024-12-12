package com.example.mustmarket.features.home.data.repository

import android.util.Log
import coil.network.HttpException
import com.example.mustmarket.core.retryConfig.RetryUtil
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.di.IODispatcher
import com.example.mustmarket.features.auth.datastore.SessionManager
import com.example.mustmarket.features.home.data.local.db.ProductDao
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import com.example.mustmarket.features.home.data.mapper.toNetworkProducts
import com.example.mustmarket.features.home.data.mapper.toProductListingEntities
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.secureStorage.SecureProductStorage
import com.example.mustmarket.features.home.secureStorage.StorageKeys.BATCH_SIZE
import com.example.mustmarket.features.home.secureStorage.StorageKeys.CACHE_DURATION
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class AllProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val dao: ProductDao,
    private val preferences: SecureProductStorage,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val retryUtil: RetryUtil,
    private val sessionManager: SessionManager
) : AllProductsRepository {


    private val cacheMutex = Mutex()

    override suspend fun shouldRefresh(): Boolean = withContext(ioDispatcher) {
        val lastUpdate = preferences.getLastUpdateTimestamp()
        val currentTime = System.currentTimeMillis()
        val cacheExpired = (currentTime - lastUpdate) > CACHE_DURATION.hours.inWholeMilliseconds
        val cacheEmpty = dao.getAllProducts().firstOrNull()?.isEmpty() ?: true
        cacheEmpty || cacheExpired
    }

    override suspend fun getAllProducts(forceRefresh: Boolean): Flow<Resource<List<NetworkProduct>>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val cachedProducts = dao.getAllProducts().firstOrNull()

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
                val tokenResult = sessionManager.fetchAccessToken()
                val response = retryUtil.executeWithRetry { productsApi.getAllProducts() }

                if (response.message == "Success") {
                    val processedProducts = response.data.map { it.toDomainProduct() }
                        .filter { it.brand.isNotBlank() }
                        .sortedByDescending { it.id }
                        .distinctBy { it.id }

                    cacheMutex.withLock {
                        dao.clearAllProducts()
                        processedProducts.chunked(BATCH_SIZE).forEach { batch ->
                            dao.insertProducts(batch.toProductListingEntities())
                        }
                    }

                    preferences.updateLastUpdateTimestamp()

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

    override suspend fun getProductsById(productId: Int): Flow<Resource<NetworkProduct>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val response = retryUtil.executeWithRetry {
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
        }.flowOn(ioDispatcher)

    override suspend fun refreshProducts(): Flow<Resource<List<NetworkProduct>>> = flow {
        emit(Resource.Loading(true))
        try {
            cacheMutex.withLock {
                emit(fetchAndProcessProducts())
            }
            emit(Resource.Loading(false))
        } catch (e: Exception) {
            emit(handleError(e))
        }
    }.flowOn(ioDispatcher)


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