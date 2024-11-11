package com.example.mustmarket.features.auth.presentation.login.state

data class LoginState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val passwordInput: String = "",
    val emailInput: String = "",
    val result: String = "",
    val showPassword: Boolean = false,
    val emailError: String = "",
    val passwordError: String = "",
    val authState: AuthState = AuthState.LOGGED_OUT
)