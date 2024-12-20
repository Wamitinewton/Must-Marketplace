package com.example.mustmarket.database.mappers

import com.example.mustmarket.database.entities.UserEntity
import com.example.mustmarket.features.auth.domain.model.AuthedUser

fun UserEntity.toAuthedUser(): AuthedUser {
    return AuthedUser(
        id = id,
        name = name,
        email = email,
    )
}

fun AuthedUser.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        number = number
    )
}
