package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AllProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi
) : AllProductsRepository {
    override suspend fun getAllProducts(): Flow<Resource<List<NetworkProduct>>> = flow {

        try {
            emit(Resource.Loading(true))
            val response = productsApi.getAllProducts()
            if (response.message == "Success"){
                val products = response.data
                    .filter { it.brand.isNotBlank() }
                    .map { it.toDomainProduct() }


                if (products.isEmpty()){
                    emit(Resource.Error(
                        message = "No products available"
                    ))
                } else {
                    emit(
                        Resource.Success(
                            data = products
                        )
                    )
                }
            } else {
                emit(
                    Resource.Error(
                        message = "API Error: ${response.message}"
                    )
                )
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
        } finally {
            emit(Resource.Loading(false))
        }
    }
}