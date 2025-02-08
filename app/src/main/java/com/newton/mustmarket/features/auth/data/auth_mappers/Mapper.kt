package com.newton.mustmarket.features.auth.data.auth_mappers

import com.newton.mustmarket.features.auth.data.remote.auth_response.LogInResultDto
import com.newton.mustmarket.features.auth.data.remote.auth_response.LoginDataDto
import com.newton.mustmarket.features.auth.data.remote.auth_response.RefreshTokenDto
import com.newton.mustmarket.features.auth.data.remote.auth_response.UserDto
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.auth.domain.model.LoginData
import com.newton.mustmarket.features.auth.domain.model.LoginResult
import com.newton.mustmarket.features.auth.domain.model.RefreshToken

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

internal fun AuthedUser.toUserDto(): UserDto {
    return UserDto(
        email = email,
        id = id,
        name = name,
    )
}

