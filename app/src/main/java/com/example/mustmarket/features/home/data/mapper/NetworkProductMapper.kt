package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.features.home.data.local.converters.ProductConverters
import com.example.mustmarket.features.home.data.local.entities.ProductListingEntity
import com.example.mustmarket.features.home.data.remote.dto.CategoryDto
import com.example.mustmarket.features.home.data.remote.dto.NetworkProductDto
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory


fun NetworkProductDto.toDomainProduct(): NetworkProduct {
    return NetworkProduct(
        id = id,
        name = name ?: brand,
        price = price,
        inventory = inventory,
        brand = brand,
        description = description ?: "No description available",
        category = category.toDomainCategory(),
        images = images
    )
}

fun CategoryDto.toDomainCategory(): ProductCategory {
    return ProductCategory(
        id = id,
        name = name,
        categoryImage = image
    )
}

fun NetworkProduct.toProductListingEntity(): ProductListingEntity {
    return ProductListingEntity(
        id = id,
        name = name,
        brand = brand,
        price = price,

        images = ProductConverters().toStringImageList(images),
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
        images = ProductConverters().fromStringImageList(images),
    )
}

fun List<ProductListingEntity>.toNetworkProducts(): List<NetworkProduct> =
    map { it.toNetworkProduct() }

fun List<NetworkProduct>.toProductListingEntities(): List<ProductListingEntity> =
    map { it.toProductListingEntity() }



