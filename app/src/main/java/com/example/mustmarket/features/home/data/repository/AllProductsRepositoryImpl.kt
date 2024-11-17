package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.RepositoryError
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.di.IODispatcher
import com.example.mustmarket.features.home.data.local.db.ProductDao
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import com.example.mustmarket.features.home.data.mapper.toNetworkProducts
import com.example.mustmarket.features.home.data.mapper.toProductListingEntities
import com.example.mustmarket.features.home.secureStorage.SecureProductStorage
import com.example.mustmarket.features.home.secureStorage.StorageKeys.BATCH_SIZE
import com.example.mustmarket.features.home.secureStorage.StorageKeys.CACHE_DURATION
import com.example.mustmarket.features.home.secureStorage.StorageKeys.INITIAL_RETRY_DELAY
import com.example.mustmarket.features.home.secureStorage.StorageKeys.MAX_RETRY_ATTEMPTS
import com.example.mustmarket.features.home.secureStorage.StorageKeys.RETRY_FACTOR
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AllProductsRepository {

    companion object {

    }

    private val cacheMutex = Mutex()

    override suspend fun shouldRefresh(): Boolean = withContext(ioDispatcher) {
        val lastUpdate = preferences.getLastUpdateTimestamp()
        val currentTime = System.currentTimeMillis()
        val cacheExpired = (currentTime - lastUpdate) > CACHE_DURATION.hours.inWholeMilliseconds
        val cacheEmpty = dao.getAllProducts().firstOrNull()?.isEmpty() ?: true
        cacheEmpty || cacheExpired
    }

    private suspend fun processAndCacheProducts(
        rawProducts: List<NetworkProduct>
    ): Resource<List<NetworkProduct>> {
        return try {
            val processedProducts = rawProducts
                .filter { it.brand.isNotBlank() }
                .sortedByDescending { it.id }

            dao.clearAllProducts()

            processedProducts
                .chunked(BATCH_SIZE)
                .forEach { batch ->
                    dao.insertProducts(batch.toProductListingEntities())
                }

            preferences.updateLastUpdateTimestamp()

            val cachedProducts = dao.getAllProducts().firstOrNull()
            if (!cachedProducts.isNullOrEmpty()) {
                Resource.Success(cachedProducts.toNetworkProducts())
            } else {
                Resource.Error("No products found")
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun fetchAndCacheProducts(): Resource<List<NetworkProduct>> =
        withContext(ioDispatcher) {
            try {
                retryWithExponentialBackoff {
                    val response = productsApi.getAllProducts()

                    if (response.message == "Success") {
                        cacheMutex.withLock {
                            processAndCacheProducts(response.data.map { it.toDomainProduct() })
                        }
                    } else {
                        Resource.Error(response.message)
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }

    override suspend fun getAllProducts(forceRefresh: Boolean): Flow<Resource<List<NetworkProduct>>> =

        flow {
            emit(Resource.Loading(true))
            try {
                val cachedProducts = dao.getAllProducts().firstOrNull()

                val shouldRefresh = forceRefresh || shouldRefresh()
                if (shouldRefresh) {
                    when (val fetchResult = fetchAndCacheProducts()) {
                        is Resource.Success -> {
                            emit(Resource.Success(fetchResult.data))
                        }

                        is Resource.Error -> {
                            if (cachedProducts.isNullOrEmpty()) {
                                emit(
                                    Resource.Error(
                                        fetchResult.message ?: "Unknown error occurred"
                                    )
                                )


                            }
                        }

                        else -> {}
                    }
                    emit(Resource.Loading(false))

                } else if (!cachedProducts.isNullOrEmpty()) {
                    emit(Resource.Success(cachedProducts.toNetworkProducts()))
                }
                emitAll(
                    dao.getAllProducts()
                        .map { entities ->
                            if (entities.isEmpty()) {
                                Resource.Error("No products found")
                            } else {
                                Resource.Success(entities.toNetworkProducts())
                            }
                        }
                        .flowOn(ioDispatcher)
                )

            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error occurred"))
            }
        }.flowOn(ioDispatcher)

    override suspend fun getProductsById(productId: Int): Flow<Resource<NetworkProduct>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val response = retryWithExponentialBackoff {
                    productsApi.getProductsById(productId)
                }
                emit(Resource.Loading(false))
                emit(
                    if (response.message == "Success") {
                        Resource.Success(response.data.toDomainProduct())

                    } else {
                        Resource.Error(response.message)
                    }
                )

            } catch (e: Exception) {
                emit(handleError(e))
            }
        }.flowOn(ioDispatcher)

    override suspend fun refreshProducts(): Flow<Resource<List<NetworkProduct>>> = flow {
        emit(Resource.Loading(true))
        try {
            cacheMutex.withLock {
                emit(fetchAndCacheProducts())
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

    private suspend fun <T> retryWithExponentialBackoff(
        times: Int = MAX_RETRY_ATTEMPTS,
        initialDelay: Long = INITIAL_RETRY_DELAY,
        factor: Double = RETRY_FACTOR,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                if (e is RepositoryError) throw e

                kotlinx.coroutines.delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong()
            }
        }
        return block()
    }
}