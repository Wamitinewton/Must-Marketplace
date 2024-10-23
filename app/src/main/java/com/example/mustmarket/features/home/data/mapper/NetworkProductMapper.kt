package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.features.home.data.local.entities.ProductListingEntity
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
        name = name,
    )
}

fun NetworkProduct.toProductListingEntity(): ProductListingEntity {
    return ProductListingEntity(
        id = id,
        name = name,
        brand = brand,
        price = price,
        image = imageUrl ?: "",
        inventory = inventory,
        description = description,
        category = category,
        lastUpdated = System.currentTimeMillis(),
    )
}

fun ProductListingEntity.toNetworkProduct(): NetworkProduct {
    return NetworkProduct(
        id = id ?: -1,
        name = name,
        price = price,
        inventory = inventory,
        brand = brand,
        description = description,
        category = category,
        imageUrl = image,
    )
}

fun List<ProductListingEntity>.toNetworkProducts(): List<NetworkProduct> =
    map { it.toNetworkProduct() }

fun List<NetworkProduct>.toProductListingEntities(): List<ProductListingEntity> =
    map { it.toProductListingEntity() }



