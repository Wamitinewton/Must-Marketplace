package com.newton.mustmarket.features.merchant.get_merchants.data.repository

import coil.network.HttpException
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.create_store.data.remote.api_service.MerchantServices
import com.newton.mustmarket.features.merchant.get_merchants.data.mappers.toGetDomainMerchants
import com.newton.mustmarket.features.merchant.get_merchants.domain.model.GetMerchantsData
import com.newton.mustmarket.features.merchant.get_merchants.domain.repository.GetMerchantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetMerchantsRepositoryImpl @Inject constructor(
    private val merchantsService: MerchantServices
): GetMerchantsRepository {
    override suspend fun getAllMerchants(): Flow<Resource<List<GetMerchantsData>>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = merchantsService.getAllMerchants()

            if (response.message == "Success") {
                val merchants = response.dataDto?.map { it.toGetDomainMerchants() }
                emit(Resource.Success(merchants))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Network error. Try again later"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "An error has occurred"))
        } catch (e: Exception) {
            emit(Resource.Error("An error has occurred. ${e.message}"))
        }
    }

    override suspend fun getMerchantById(id: Int): Flow<Resource<GetMerchantsData>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = merchantsService.getMerchantById(id)
            emit(Resource.Success(data = response.toGetDomainMerchants()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Network error. Try again later"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "An error has occurred"))
        } catch (e: Exception) {
            emit(Resource.Error("An error has occurred. ${e.message}"))
        }
    }
}