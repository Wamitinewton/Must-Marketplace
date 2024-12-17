package com.example.mustmarket.features.auth.data.mapper

import com.example.mustmarket.features.auth.data.dto.LogInResultDto
import com.example.mustmarket.features.auth.data.dto.LoginDataDto
import com.example.mustmarket.features.auth.data.dto.RefreshTokenDto
import com.example.mustmarket.features.auth.data.dto.UserDto
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginData
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.RefreshToken

internal fun LogInResultDto.toLoginResult(): LoginResult {
    return LoginResult(
        message = message,
        data = data.toLoginData()
    )
}


internal fun LoginDataDto.toLoginData(): LoginData {
    return LoginData(
        token = token,
        refreshToken = refreshToken,
        user = user.toAuthedUser()
    )
}

fun RefreshTokenDto.toRefreshToken(): RefreshToken {
    return RefreshToken(
        refreshToken = refreshToken
    )
}

fun RefreshToken.toRefreshTokenDto(): RefreshTokenDto {
    return RefreshTokenDto(
        refreshToken = refreshToken
    )
}


internal fun UserDto.toAuthedUser(): AuthedUser {
    return AuthedUser(
        email = email,
        id = id,
        name = name,
    )
}

