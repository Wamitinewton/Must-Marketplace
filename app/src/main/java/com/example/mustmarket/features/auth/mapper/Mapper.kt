package com.example.mustmarket.features.auth.mapper

import com.example.mustmarket.features.auth.data.dto.LogInResultDto
import com.example.mustmarket.features.auth.data.dto.LoginDataDto
import com.example.mustmarket.features.auth.data.dto.UserDto
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginDto
import com.example.mustmarket.features.auth.domain.model.LoginResult

internal fun LogInResultDto.toLoginResult(): LoginResult {
    return LoginResult(
        message = message,
        data = data.toLoginData()
    )
}

internal fun LoginDataDto.toLoginData(): LoginDto {
    return LoginDto(
        token = token,
        refreshToken = refreshToken,
        user = user.toAuthedUser()
    )
}


internal fun UserDto.toAuthedUser(): AuthedUser {
    return AuthedUser(
        email = email,
        id = id,
        name = name,
    )
}

