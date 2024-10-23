package com.example.mustmarket

import com.example.mustmarket.features.auth.domain.usecases.LoginUseCase
import com.example.mustmarket.features.auth.domain.usecases.SignUpUseCase
import com.example.mustmarket.features.auth.domain.usecases.TokenSession
import com.example.mustmarket.features.home.domain.usecases.AllProducts
import com.example.mustmarket.features.home.domain.usecases.Categories
import com.example.mustmarket.features.home.domain.usecases.ProductCategories
import com.example.mustmarket.features.home.domain.usecases.RefreshProduct

data class UseCases(
    val signUpUseCase: SignUpUseCase,
    val loginUseCase: LoginUseCase,
    val tokenLogin: TokenSession,
    val productCategories: ProductCategories,
    val categories: Categories,
    val allProducts: AllProducts,
    val refreshProduct: RefreshProduct
)