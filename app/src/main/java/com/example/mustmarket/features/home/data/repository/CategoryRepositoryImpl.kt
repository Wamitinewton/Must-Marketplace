package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.data.mapper.toProductCategory
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.ProductCategory
import com.example.mustmarket.features.home.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: ProductsApi
) : CategoryRepository {
    override suspend fun getCategories(size: Int): Flow<Resource<List<ProductCategory>>> = flow {
        emit(Resource.Loading())
        try {
            val response = categoryApi.getCategories(size)
            emit(Resource.Success(data = response))
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
    }

    override suspend fun getAllCategories(): Flow<Resource<List<ProductCategory>>> = flow {

        try {
            emit(Resource.Loading(true))
            val response = categoryApi.getAllCategories()
            if (response.message == "Success") {
                val categories = response.data
                    .map { it.toProductCategory() }

                if (categories.isEmpty()) {
                    emit(
                        Resource.Error(
                            message = "No categories found"
                        )
                    )
                } else {
                    emit(
                        Resource.Success(
                            data = categories
                        )
                    )
                }
            } else {
                emit(
                    Resource.Error(
                        message = response.message
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
        }finally {
            emit(Resource.Loading(false))
        }
    }
}