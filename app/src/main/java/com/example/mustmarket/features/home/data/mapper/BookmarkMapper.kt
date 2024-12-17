package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.database.converters.DataConverters
import com.example.mustmarket.database.entities.BookmarkedProductEntity
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct

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