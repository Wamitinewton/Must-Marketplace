package com.example.mustmarket.features.home.data.paging.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.mustmarket.core.threadExecutor.ThreadPoolExecutor
import com.example.mustmarket.core.threadExecutor.OperationType
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.features.home.data.mapper.toDomainProduct
import com.example.mustmarket.features.home.data.mapper.toProductListingEntities
import com.example.mustmarket.features.home.data.paging.pagingConsts.PagingConsts
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val productsApi: ProductsApi,
    private val dao: ProductDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val threadExecutor: ThreadPoolExecutor
) : RemoteMediator<Int, NetworkProduct>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NetworkProduct>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.id
                }
            }

            val response = threadExecutor.executeIsolatedThread(
                operationType = OperationType.NETWORK,
                timeOuts = PagingConsts.NETWORK_TIMEOUT
            ) {
                productsApi.getAllProducts(
                    //loadKey!!,
                )
            }.first()

            if (response.message == "Success") {
                withContext(ioDispatcher) {
                    if (loadType == LoadType.REFRESH) {
                        dao.clearAllProducts()
                    }
                    val products = response.data.map { it.toDomainProduct() }
                        .filter { it.brand.isNotBlank() }
                        .sortedByDescending { it.id }
                        .distinctBy { it.id }
                        .toProductListingEntities()

                    dao.insertProducts(products)
                }

                MediatorResult.Success(
                    endOfPaginationReached = response.data.isEmpty()
                )
            } else {
                MediatorResult.Error(Exception(response.message))
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}