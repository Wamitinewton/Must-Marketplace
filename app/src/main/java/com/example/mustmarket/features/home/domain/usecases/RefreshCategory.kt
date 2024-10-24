package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.features.home.domain.repository.CategoryRepository

class RefreshCategory(private val repository: CategoryRepository) {
    suspend operator fun invoke() = repository.refreshCategories()
}