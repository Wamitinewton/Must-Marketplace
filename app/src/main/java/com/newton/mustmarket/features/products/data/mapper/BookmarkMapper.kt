package com.newton.mustmarket.features.products.data.mapper

import com.newton.mustmarket.database.converters.DataConverters
import com.newton.mustmarket.database.entities.BookmarkedProductEntity
import com.newton.mustmarket.features.products.domain.model.products.NetworkProduct

fun NetworkProduct.toBookmarkedProduct(): BookmarkedProductEntity {
    return BookmarkedProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        images = DataConverters().toStringImageList(images),
        inventory = inventory,
        category = category,
        brand = brand,
        userData = userData
    )
}

fun BookmarkedProductEntity.toNetworkProduct(): NetworkProduct {
    return NetworkProduct(
        id = id,
        name = name,
        description = description,
        price = price,
        images = DataConverters().fromStringImageList(images),
        inventory = inventory,
        category = category,
        brand = brand,
        userData = userData
    )
}