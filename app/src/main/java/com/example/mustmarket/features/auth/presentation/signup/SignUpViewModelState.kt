package com.example.mustmarket.features.auth.presentation.signup

data class SignUpState(
    val isLoading: Boolean,
    val errorMessage: String,
    val nameInput: String,
    val passwordInput: String,
    val passwordConfirmInput: String,
    val emailInput: String,
    val result: String,
    val showPassword: Boolean,
    val emailError: String,
    val passwordError: String
)

data class SignUpViewModelState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val nameInput: String = "",
    val passwordInput: String = "",
    val passwordConfirmInput: String = "",
    val emailInput: String = "",
    val result: String = "",
    val showPassword: Boolean = false,
    val emailError: String = "",
    val passwordError: String = ""
) {
    fun toUiState(): SignUpState = SignUpState(
        isLoading,
        errorMessage,
        nameInput,
        passwordInput,
        passwordConfirmInput,
        emailInput,
        result,
        showPassword,
        emailError,
        passwordError
    )
}
