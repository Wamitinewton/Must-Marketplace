package com.example.mustmarket.features.auth.presentation.login

data class LoginScreenState(
    val isLoading: Boolean,
    val errorMessage: String,
    val passwordInput: String,
    val emailInput: String,
    val result: String,
    val showPassword: Boolean,
    val emailError: String,
    val passwordError: String
)

data class LoginViewModelState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val passwordInput: String = "",
    val emailInput: String = "",
    val result: String = "",
    val showPassword: Boolean = false,
    val emailError: String = "",
    val passwordError: String = ""
) {
    fun toUiState(): LoginScreenState =
        LoginScreenState(
            isLoading,
            errorMessage,
            passwordInput,
            emailInput,
            result,
            showPassword,
            emailError,
            passwordError
        )
}
