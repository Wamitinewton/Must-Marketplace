package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.features.home.data.local.entities.CategoryListingEntity
import com.example.mustmarket.features.home.data.remote.dto.CategoryDto
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory

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
        image = categoryImage,
    )
}

fun CategoryListingEntity.toProductCategory(): ProductCategory {
    return ProductCategory(
        id = id,
        name = name,
        categoryImage = image
    )
}

fun List<CategoryListingEntity>.toProductCategory(): List<ProductCategory> =
    map { it.toProductCategory() }

fun List<ProductCategory>.toCategoryListingEntity(): List<CategoryListingEntity> =
    map { it.toCategoryListingEntity() }
