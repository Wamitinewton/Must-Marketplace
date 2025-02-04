package com.newton.mustmarket.features.merchant.store.data.mapper

import com.newton.mustmarket.features.auth.data.auth_mappers.toUserDto
import com.newton.mustmarket.features.home.data.mapper.toProductDto
import com.newton.mustmarket.features.home.data.remote.response.NetworkProductDto
import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.merchant.store.data.remote.response.CreateMerchantsData
import com.newton.mustmarket.features.merchant.store.domain.models.MerchantResponseData

fun MerchantResponseData.toMerchantDto(): CreateMerchantsData {
    return CreateMerchantsData(
        id = storeId,
        name = name,
        userDto = user.toUserDto(),
        banner = banner,
        address = location,
        profile = profile,
        description = description,
        products = products?.productDto(),
        reviews = reviews,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<NetworkProduct>.productDto(): List<NetworkProductDto> =
    map { it.toProductDto() }
