package com.newton.mustmarket.features.auth.presentation.signup.event

sealed class SignupEvent {
    data class EmailChanged(val email: String) : SignupEvent()
    data class UsernameChanged(val username: String) : SignupEvent()
    data class PasswordChanged(val password: String) : SignupEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignupEvent()
    data class TogglePasswordVisibility(val show: Boolean) : SignupEvent()
    data class ToggleConfirmPasswordVisibility(val show: Boolean) : SignupEvent()
    data object Signup : SignupEvent()
    data object ClearError : SignupEvent()
}