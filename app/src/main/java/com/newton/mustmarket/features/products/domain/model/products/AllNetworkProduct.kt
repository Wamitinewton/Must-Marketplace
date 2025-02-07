package com.newton.mustmarket.features.products.domain.model.products

import kotlinx.serialization.Serializable


@Serializable
data class AllNetworkProduct(
    val message: String,
    val data: List<NetworkProduct>
)
