package com.newton.mustmarket.features.home.domain.model.products

import kotlinx.serialization.Serializable


@Serializable
data class ProductInventory(
    val id: String,
    val quantity: Int
)
