package com.newton.mustmarket.features.merchant.upload_products.domain.models

import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.get_products.domain.model.categories.ProductCategory
import kotlinx.serialization.Serializable


@Serializable
data class UploadData(
    val brand: String,
    val category: ProductCategory,
    val description: String,
    val id: Int,
    val images: List<String>,
    val inventory: Int,
    val name: String,
    val price: Int,
    val user: AuthedUser?
)

