package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.core.util.Constants.BASE_URL
import com.example.mustmarket.features.home.data.remote.dto.CategoryDto
import com.example.mustmarket.features.home.data.remote.dto.NetworkProductDto
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.model.ProductCategory


fun NetworkProductDto.toDomainProduct(): NetworkProduct {
    return NetworkProduct(
        id = id,
        name = name ?: brand,
        price = price,
        inventory = inventory,
        brand = brand,
        description = description ?: "No description available",
        category = category.toDomainCategory(),
        imageUrl = images.firstOrNull()?.downloadUrl?.let { "/api/v1/images/image/download/5" }
    )
}

fun CategoryDto.toDomainCategory(): ProductCategory {
    return ProductCategory(
        id = id,
        name = name
    )
}

