package com.newton.mustmarket.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.get_products.domain.model.categories.ProductCategory


@Entity(tableName = "products")
data class ProductListingEntity(
    val name: String,
    val brand: String,
    val price: Double,
    val image: List<String> = emptyList(),
    val images: String,
    val inventory: Int,
    val description: String,
    val category: ProductCategory,
    val lastUpdated: Long = System.currentTimeMillis(),
    @PrimaryKey val id: Int? = null,
   val userData: AuthedUser
)
