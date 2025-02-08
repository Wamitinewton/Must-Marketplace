package com.newton.mustmarket.features.merchant.create_store.data.repository

import coil.network.HttpException
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.create_store.data.remote.api_service.MerchantServices
import com.newton.mustmarket.features.merchant.create_store.data.remote.response.MerchantResponse
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest
import com.newton.mustmarket.features.merchant.create_store.domain.repository.MerchantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MerchantRepositoryImpl @Inject constructor(
    private val merchantServices: MerchantServices
): MerchantRepository {
    override suspend fun addMerchant(addMerchantRequest: CreateMerchantRequest): Flow<Resource<MerchantResponse>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = merchantServices.addMerchant(addMerchantRequest)

            if (response.message == "Success") {
                emit(Resource.Success(data = response))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Http error. Try again later"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Network error."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }
}