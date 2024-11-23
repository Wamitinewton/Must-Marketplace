package com.example.mustmarket.features.home.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory


@Entity(tableName = "products")
data class ProductListingEntity(
    val name: String,
    val brand: String,
    val price: Double,
    val image: String,
    val inventory: Int,
    val description: String,
    val category: ProductCategory,
    val lastUpdated: Long = System.currentTimeMillis(),
    @PrimaryKey val id: Int? = null
)
