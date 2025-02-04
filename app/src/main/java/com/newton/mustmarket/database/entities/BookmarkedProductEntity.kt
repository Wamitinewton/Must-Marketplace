package com.newton.mustmarket.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.home.domain.model.categories.ProductCategory


@Entity(tableName = "bookmarked_products")
data class BookmarkedProductEntity(
    @PrimaryKey()
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val images: String,
    val category: ProductCategory,
    val inventory: Int,
    val brand: String,
    val bookmarkedAt: Long = System.currentTimeMillis(),
    val userData: AuthedUser
)
