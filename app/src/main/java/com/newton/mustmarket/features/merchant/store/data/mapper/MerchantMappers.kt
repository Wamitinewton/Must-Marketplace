package com.newton.mustmarket.features.merchant.store.data.mapper

import com.newton.mustmarket.features.home.data.mapper.toDomainProduct
import com.newton.mustmarket.features.home.data.mapper.toUser
import com.newton.mustmarket.features.home.data.remote.response.NetworkProductDto
import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.merchant.store.data.remote.response.MerchantResponseData
import com.newton.mustmarket.features.merchant.store.domain.models.MerchantResponse


fun MerchantResponseData.toMerchantData(): MerchantResponse {
    return MerchantResponse(
        storeId = id,
        name = name,
        user = userDto.toUser(),
        banner = banner,
        location = address,
        profile = profile,
        description = description,
        products = products?.productData(),
        reviews = reviews,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<NetworkProductDto>.productData(): List<NetworkProduct> =
    map { it.toDomainProduct() }
