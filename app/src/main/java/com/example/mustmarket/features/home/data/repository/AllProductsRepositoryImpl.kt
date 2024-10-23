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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AllProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val dao: ProductDao
) : AllProductsRepository {
    override suspend fun getAllProducts(): Flow<Resource<List<NetworkProduct>>> = flow {

        try {
            emit(Resource.Loading(true))
            dao.getAllProducts()
                .map { entities ->
                    Resource.Success(
                        entities.toNetworkProducts()
                    )
                }
                .collect { cachedData ->
                    emit(cachedData)
                }


        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message.toString()
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.message.toString()
                )
            )
        }
        try {
            val response = productsApi.getAllProducts()
            if (response.message == "Success") {
                val products = response.data
                    .filter { it.brand.isNotBlank() }
                    .map { it.toDomainProduct() }
                    .sortedByDescending { it.id }

                dao.clearAllProducts()
                dao.insertProducts(products.toProductListingEntities())

                emit(Resource.Loading(false))
                if (products.isEmpty()) {
                    emit(Resource.Error("No products found"))
                } else {
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

    override suspend fun refreshProducts(): Flow<Resource<List<NetworkProduct>>> = flow {
        try {
            emit(Resource.Loading(true))
            val response = productsApi
                .getAllProducts()
            if (response.message == "Success") {
                val products = response.data
                    .filter { it.brand.isNotBlank() }
                    .map { it.toDomainProduct() }
                    .sortedByDescending { it.id }

                dao.clearAllProducts()
                dao.insertProducts(products.toProductListingEntities())

                emit(Resource.Loading(false))
                if (products.isEmpty()) {
                    emit(Resource.Error("No products found"))
                } else {
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
        } finally {
            emit(Resource.Loading(false))
        }
    }
}