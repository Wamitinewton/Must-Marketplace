package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.features.home.data.local.entities.CategoryListingEntity
import com.example.mustmarket.features.home.data.local.entities.ProductListingEntity
import com.example.mustmarket.features.home.data.remote.dto.CategoryDto
import com.example.mustmarket.features.home.data.remote.dto.CategoryResponseDto
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.domain.model.ProductCategory

fun CategoryDto.toProductCategory(): ProductCategory {
    return ProductCategory(
        id = id,
        name = name,
        categoryImage = image
    )
}

fun ProductCategory.toCategoryListingEntity(): CategoryListingEntity {
    return CategoryListingEntity(
        id = id,
        name = name,
    )
}

fun CategoryListingEntity.toProductCategory(): ProductCategory {
    return ProductCategory(
        id = id ?: -1,
        name = name,
        categoryImage = image
    )
}

fun List<CategoryListingEntity>.toProductCategory(): List<ProductCategory> =
    map { it.toProductCategory() }

fun List<ProductCategory>.toCategoryListingEntity(): List<CategoryListingEntity> =
    map { it.toCategoryListingEntity() }
