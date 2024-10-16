package com.example.mustmarket.features.auth.domain.model

data class FinalUser(
    val user: User
)

data class ShoppingSession(
    val id: String,
    val total: Int
)

data class User(
    val userName: String = "",
    val email: String = "",
    val firstName: String = "",
    val shoppingSession: ShoppingSession
)
