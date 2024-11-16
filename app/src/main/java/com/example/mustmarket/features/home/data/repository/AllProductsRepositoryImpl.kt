package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.data.local.db.ProductDao
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import com.example.mustmarket.features.home.data.mapper.toNetworkProducts
import com.example.mustmarket.features.home.data.mapper.toProductListingEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AllProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val dao: ProductDao
) : AllProductsRepository {


    override suspend fun shouldRefresh(): Boolean {
        return dao.getAllProducts().firstOrNull()?.isEmpty() ?: true
    }

    override suspend fun fetchAndCacheProducts(): Resource<List<NetworkProduct>> {
        return try {
            val response = productsApi.getAllProducts()
            if (response.message == "Success") {
                val products = response.data
                    .filter { it.brand.isNotBlank() }
                    .map { it.toDomainProduct() }
                    .sortedByDescending { it.id }

                dao.clearAllProducts()
                dao.insertProducts(products.toProductListingEntities())

                val cachedProducts = dao.getAllProducts().firstOrNull()
                if (cachedProducts.isNullOrEmpty()) {
                    Resource.Error("Failed to cache products")
                } else {
                    Resource.Success(cachedProducts.toNetworkProducts())
                }
            } else {
                Resource.Error(response.message)
            }
        } catch (e: HttpException) {
            Resource.Error("Network error: ${e.message}")
        } catch (e: IOException) {
            Resource.Error("IO error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unknown error: ${e.message}")
        }
    }

    override suspend fun getAllProducts(): Flow<Resource<List<NetworkProduct>>> = flow {
        if (shouldRefresh()) {
            emit(Resource.Loading(true))
            val refreshResult = fetchAndCacheProducts()
            emit(refreshResult)
            emit(Resource.Loading(false))
        }

        dao.getAllProducts()
            .map { entities ->
                if (entities.isEmpty()) {
                    Resource.Error("No products found")
                } else {
                    Resource.Success(entities.toNetworkProducts())
                }
            }
            .collect { cachedData ->
                emit(cachedData)
            }
    }

    override suspend fun getProductsById(productId: Int): Flow<Resource<NetworkProduct>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val response = productsApi.getProductsById(productId)
                emit(Resource.Loading(false))
                if (response.message == "Success") {
                    val productsDetails = response.data.toDomainProduct()
                    emit(Resource.Success(data = productsDetails))
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

    override suspend fun refreshProducts(): Flow<Resource<List<NetworkProduct>>> = flow {
        emit(Resource.Loading(true))
        val result = fetchAndCacheProducts()
        emit(result)
        emit(Resource.Loading(false))
    }
}