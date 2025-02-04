package com.newton.mustmarket.features.home.data.mapper

import com.newton.mustmarket.database.entities.CategoryListingEntity
import com.newton.mustmarket.features.home.data.remote.response.CategoryDto
import com.newton.mustmarket.features.home.domain.model.categories.ProductCategory

fun CategoryDto.toProductCategory(): ProductCategory {
    return ProductCategory(
        id = id,
        name = name,
        categoryImage = image
    )
}

fun ProductCategory.toCategoryDto(): CategoryDto {
    return CategoryDto(
        id = id,
        name = name,
        image = categoryImage
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
