package com.newton.mustmarket.features.home.data.mapper

import com.newton.mustmarket.database.converters.DataConverters
import com.newton.mustmarket.database.entities.ProductListingEntity
import com.newton.mustmarket.features.auth.data.auth_mappers.toUserDto
import com.newton.mustmarket.features.auth.data.remote.auth_response.UserDto
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.home.data.remote.response.CategoryDto
import com.newton.mustmarket.features.home.data.remote.response.NetworkProductDto
import com.newton.mustmarket.features.home.domain.model.categories.ProductCategory
import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct


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

fun NetworkProduct.toProductDto(): NetworkProductDto {
    return NetworkProductDto(
        id = id,
        name = name ?: brand,
        price = price,
        inventory = inventory,
        brand = brand,
        description = description ?: "No description available",
        category = category.toCategoryDto(),
        images = images,
        user = userData.toUserDto()
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

fun UserDto.toUser(): AuthedUser {
    return AuthedUser(
        email = email,
        id = id,
        name = name,
        number = phone
    )
}

fun List<ProductListingEntity>.toNetworkProducts(): List<NetworkProduct> =
    map { it.toNetworkProduct() }

fun List<NetworkProduct>.toProductListingEntities(): List<ProductListingEntity> =
    map { it.toProductListingEntity() }



