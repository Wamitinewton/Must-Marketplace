package com.newton.mustmarket.features.get_products.data.repository

import coil.network.HttpException
import com.newton.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.database.dao.CategoryDao
import com.newton.mustmarket.di.IODispatcher
import com.newton.mustmarket.features.get_products.data.mapper.toCategoryListingEntity
import com.newton.mustmarket.features.get_products.data.mapper.toDomainCategory
import com.newton.mustmarket.features.get_products.data.mapper.toProductCategory
import com.newton.mustmarket.features.get_products.data.remote.api_service.ProductsApi
import com.newton.mustmarket.features.get_products.data.repository.CategoryRepositoryImpl.CacheBatch.BATCH_SIZE
import com.newton.mustmarket.features.get_products.domain.model.categories.ProductCategory
import com.newton.mustmarket.features.get_products.domain.model.categories.UploadCategoryResponse
import com.newton.mustmarket.features.get_products.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: ProductsApi,
    private val dao: CategoryDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CategoryRepository {

    object CacheBatch {
        const val BATCH_SIZE = 100
    }

    private val cacheMutex = Mutex()

    override suspend fun shouldRefresh(): Boolean {
        val categories = dao.getAllCategories().firstOrNull()
        return categories.isNullOrEmpty()
    }


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


    override suspend fun getAllCategories(shouldRefresh: Boolean): Flow<Resource<List<ProductCategory>>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val cachedCategories = dao.getAllCategories().firstOrNull()

                if (!shouldRefresh && !cachedCategories.isNullOrEmpty()) {
                    emit(Resource.Success(cachedCategories.toProductCategory()))
                } else {
                    val fetchResult = fetchAndProcessCategories()
                    emit(fetchResult)
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error occurred"))
            } finally {
                emit(Resource.Loading(false))
            }
        }.flowOn(ioDispatcher)



    private suspend fun fetchAndProcessCategories(): Resource<List<ProductCategory>> =
        withContext(ioDispatcher) {
            try {
                val response = categoryApi.getAllCategories()

                if (response.message == SUCCESS_RESPONSE) {
                    val processedCategories = response.data.map { it.toDomainCategory() }
                        .sortedByDescending { it.id }
                        .distinctBy { it.id }

                    cacheMutex.withLock {
                        dao.clearAllCategory()
                        processedCategories.chunked(BATCH_SIZE).forEach { batch ->
                            dao.insertCategories(batch.toCategoryListingEntity())
                        }
                    }

                    val freshCategories = dao.getAllCategories().firstOrNull()
                    if (!freshCategories.isNullOrEmpty()) {
                        Resource.Success(freshCategories.toProductCategory())
                    } else {
                        Resource.Error("No categories found")
                    }
                } else {
                    Resource.Error(response.message)
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error has occurred")
            }
        }



    override suspend fun addCategory(
        image: File,
        name: String
    ): Flow<Resource<UploadCategoryResponse>> = flow {
        emit(Resource.Loading(true))
        try {
            val imageFile = image.toMultipartBodyPart("file")
            val response =  categoryApi.addCategory(
                name = name,
                image = imageFile
            )
            if (response.message == "Success") {
                emit(Resource.Success(response))
                emit(Resource.Loading(false))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.message}"))
        } catch (e: IOException) {
            emit(Resource.Error("IO error: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unknown error: ${e.message}"))
        }
    }


    private fun File.toMultipartBodyPart(name: String): MultipartBody.Part {
        val requestFile = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, this.name, requestFile)
    }


}