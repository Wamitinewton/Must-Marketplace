package com.example.mustmarket.features.home.data.paging.pagingSorce

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.features.home.data.mapper.toNetworkProducts
import com.example.mustmarket.features.home.data.paging.pagingConsts.PagingConsts
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ProductPagingSource(
    private val dao: ProductDao,
    private val ioDispatcher: CoroutineDispatcher
) : PagingSource<Int, NetworkProduct>() {
    override fun getRefreshKey(state: PagingState<Int, NetworkProduct>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkProduct> {
        return try {
            val page = params.key ?: PagingConsts.STARTING_PAGE_INDEX
            val pageSize = params.loadSize

            val products =
                dao.getPagedProducts(pageSize, (page - 1) * pageSize)
                    .firstOrNull()
                    ?.toNetworkProducts()
                    ?: emptyList()

            LoadResult.Page(
                data = products,
                prevKey = if (page == PagingConsts.STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}