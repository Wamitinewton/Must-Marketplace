package com.example.mustmarket.features.auth.datastore

data class UserData(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val lastLoginTimeStamp: Long? = null
)
