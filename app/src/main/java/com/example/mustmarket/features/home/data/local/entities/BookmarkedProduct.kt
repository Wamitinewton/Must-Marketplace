package com.example.mustmarket.features.home.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory


@Entity(tableName = "bookmarked_products")
data class BookmarkedProduct(
    @PrimaryKey()
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val images: String,
    val category: ProductCategory,
    val inventory: Int,
    val brand: String,
    val bookmarkedAt: Long = System.currentTimeMillis()
)
