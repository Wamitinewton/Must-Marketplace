package com.newton.mustmarket.features.merchant.get_merchants.data.mappers

import com.newton.mustmarket.features.auth.data.auth_mappers.toAuthedUser
import com.newton.mustmarket.features.get_products.data.mapper.toDomainProduct
import com.newton.mustmarket.features.get_products.data.remote.response.NetworkProductDto
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.merchant.get_merchants.data.remote.api_response.GetMerchantsDataDto
import com.newton.mustmarket.features.merchant.get_merchants.domain.model.GetMerchantsData

fun GetMerchantsDataDto.toGetDomainMerchants(): GetMerchantsData {
    return GetMerchantsData(
        id = id,
        name = name,
        user = user.toAuthedUser(),
        banner = banner,
        address = address,
        profile = profile,
        description = description,
        product = product?.toDomainProducts(),
        reviews = reviews,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun List<NetworkProductDto>.toDomainProducts(): List<NetworkProduct> =
    map { it.toDomainProduct() }