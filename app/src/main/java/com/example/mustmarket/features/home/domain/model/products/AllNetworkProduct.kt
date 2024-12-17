package com.example.mustmarket.features.home.domain.model.products

import kotlinx.serialization.Serializable


@Serializable
data class AllNetworkProduct(
    val message: String,
    val data: List<NetworkProduct>
)
