package com.example.mustmarket.features.home.domain.model

import java.sql.Timestamp

data class Category(
    val id: Int,
    val name: String,
    val images: String,
    val description: String,
    val createdAt: Timestamp,
)
