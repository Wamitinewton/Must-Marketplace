package com.example.mustmarket.features.home.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.data.local.db.CategoryDao
import com.example.mustmarket.features.home.data.mapper.toCategoryListingEntity
import com.example.mustmarket.features.home.data.mapper.toDomainCategory
import com.example.mustmarket.features.home.data.mapper.toProductCategory
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import com.example.mustmarket.features.home.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: ProductsApi,
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun getCategories(size: Int): Flow<Resource<List<ProductCategory>>> = flow {
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
            dao.getAllCategories()
                .map { entities ->
                    Resource.Success(
                        entities.toProductCategory()
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
            val response = categoryApi.getAllCategories()
            if (response.message == "Success") {
                val categories = response.data
                    .map { it.toDomainCategory() }
                    .sortedByDescending { it.id }
                dao.clearAllCategory()
                dao.insertCategories(categories.toCategoryListingEntity())
                emit(Resource.Loading(false))
                if (categories.isEmpty()) {
                    emit(Resource.Error("No categories were found"))
                } else {
                    emit(Resource.Success(categories))
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

    override suspend fun refreshCategories(): Flow<Resource<List<ProductCategory>>> = flow {

        emit(Resource.Loading(true))
        try {
            val response = categoryApi.getAllCategories()
            if (response.message == "Success") {
                val categories = response.data
                    .map { it.toDomainCategory() }
                    .sortedByDescending { it.id }
                dao.clearAllCategory()
                dao.insertCategories(categories.toCategoryListingEntity())
                emit(Resource.Loading(false))
                if (categories.isEmpty()) {
                    emit(Resource.Error("No categories were found"))
                } else {
                    emit(Resource.Success(categories))
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
}