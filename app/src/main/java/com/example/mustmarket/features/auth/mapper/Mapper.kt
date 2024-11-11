package com.example.mustmarket.features.auth.mapper

import com.example.mustmarket.features.auth.data.dto.LogInResultDto
import com.example.mustmarket.features.auth.data.dto.UserDto
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginResult

internal fun LogInResultDto.toLoginResult(): LoginResult {
    return LoginResult(
        accessToken = accessToken ?: "",
        refreshToken = refreshToken ?: ""
    )
}

internal fun UserDto.toAuthedUser(): AuthedUser {
    return AuthedUser(
        email = email ?: "",
        id = id ?: 0,
        userName = username ?: "",
    )
}

