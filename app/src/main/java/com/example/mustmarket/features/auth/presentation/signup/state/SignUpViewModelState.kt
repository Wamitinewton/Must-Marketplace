package com.example.mustmarket.features.auth.presentation.signup.state

import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength
import com.example.mustmarket.features.auth.presentation.login.state.AuthState

data class SignUpViewModelState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val passwordInput: String = "",
    val emailInput: String = "",
    val nameInput: String = "",
    val passwordConfirmInput: String = "",
    val result: AuthedUser = AuthedUser(0, "", ""),
    val showPassword: Boolean = false,
    val emailError: String = "",
    val nameError: String = "",
    val passwordError: List<String> = emptyList(),
    val passwordConfirmError: List<String> = emptyList(),
    val passwordStrength: PasswordStrength = PasswordStrength.NONE
)