package com.example.mustmarket.features.home.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class AllNetworkProduct(
    val result: List<NetworkProduct>
)
