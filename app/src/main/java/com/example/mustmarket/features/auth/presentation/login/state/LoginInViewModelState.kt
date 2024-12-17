package com.example.mustmarket.features.auth.presentation.login.state

import com.example.mustmarket.features.auth.presentation.login.enums.AuthState

data class LoginState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val passwordInput: String = "",
    val emailInput: String = "",
    val result: String = "",
    val showPassword: Boolean = false,
    val emailError: String = "",
    val passwordError: String = "",
    val authState: AuthState = AuthState.LOGGED_OUT,
    val logOutError: String? = null
)