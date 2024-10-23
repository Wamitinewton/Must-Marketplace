package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.features.home.domain.repository.CategoryRepository

class Categories(private val repository: CategoryRepository) {
    suspend operator fun invoke(size:Int) = repository.getCategories(size)
}