package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.database.converters.DataConverters
import com.example.mustmarket.database.entities.ProductListingEntity
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.home.data.remote.response.CategoryDto
import com.example.mustmarket.features.home.data.remote.response.NetworkProductDto
import com.example.mustmarket.features.home.data.remote.response.UserDataDto
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
        images = images,
        userData = user!!.toUser()
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

        images = DataConverters().toStringImageList(images),
        inventory = inventory,
        description = description,
        category = category,
        lastUpdated = System.currentTimeMillis(),
        userData = userData
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
        images = DataConverters().fromStringImageList(images),
        userData = userData
    )
}

fun UserDataDto.toUser(): AuthedUser {
    return AuthedUser(
        email = email,
        id = id,
        name = name,
        number = number
    )
}

fun List<ProductListingEntity>.toNetworkProducts(): List<NetworkProduct> =
    map { it.toNetworkProduct() }

fun List<NetworkProduct>.toProductListingEntities(): List<ProductListingEntity> =
    map { it.toProductListingEntity() }



