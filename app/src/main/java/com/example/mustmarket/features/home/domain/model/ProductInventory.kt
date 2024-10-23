package com.example.mustmarket.features.home.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class ProductInventory(
    val id: String,
    val quantity: Int
)
