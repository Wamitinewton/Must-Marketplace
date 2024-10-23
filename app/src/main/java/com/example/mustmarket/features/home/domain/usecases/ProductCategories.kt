package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.features.home.domain.repository.CategoryRepository

class ProductCategories(private val repository: CategoryRepository) {
    suspend operator fun invoke() = repository.getAllCategories()
}