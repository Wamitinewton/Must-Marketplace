package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct

fun NetworkProduct.toBookmarkedProduct(): BookmarkedProduct {
    return BookmarkedProduct(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl ?: "",
        inventory = inventory,
        category = category,
        brand = brand
    )
}

fun BookmarkedProduct.toNetworkProduct(): NetworkProduct {
    return NetworkProduct(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl ?: "",
        inventory = inventory,
        category = category,
        brand = brand
    )
}