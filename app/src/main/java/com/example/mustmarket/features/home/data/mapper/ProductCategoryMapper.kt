package com.example.mustmarket.features.home.data.mapper

import com.example.mustmarket.features.home.data.remote.dto.CategoryDto
import com.example.mustmarket.features.home.data.remote.dto.CategoryResponseDto
import com.example.mustmarket.features.home.domain.model.ProductCategory

fun CategoryDto.toProductCategory(): ProductCategory {
    return ProductCategory(
        id = id,
        name = name
    )
}