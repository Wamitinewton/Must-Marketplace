package com.example.mustmarket.features.home.domain.usecases

import com.example.mustmarket.features.home.domain.repository.AllProductsRepository

class AllProducts(private val repository: AllProductsRepository) {
    suspend operator fun invoke() = repository.getAllProducts()
}