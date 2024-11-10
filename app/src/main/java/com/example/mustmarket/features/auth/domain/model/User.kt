package com.example.mustmarket.features.auth.domain.model

data class FinalUser(
    val user: User
)



data class User(
    val email: String = "",
    val firstName: String = "",
)
