package com.newton.mustmarket.database.mappers

import com.newton.mustmarket.database.entities.UserEntity
import com.newton.mustmarket.features.auth.domain.model.AuthedUser

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
