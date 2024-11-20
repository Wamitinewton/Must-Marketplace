package com.example.mustmarket.features.auth.presentation.signup.state

import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.presentation.login.state.AuthState

data class SignUpViewModelState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val passwordInput: String = "",
    val emailInput: String = "",
    val nameInput: String = "",
    val passwordConfirmInput: String = "",
    val result: AuthedUser? = null,
    val showPassword: Boolean = false,
    val emailError: String = "",
    val nameError: String = "",
    val passwordConfirmError: String = "",
    val passwordError: String = "",
)