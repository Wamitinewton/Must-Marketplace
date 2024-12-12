package com.example.mustmarket.features.auth.presentation.login.event

sealed class LoginEvent {

    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class TogglePasswordVisibility(val show: Boolean) : LoginEvent()
    data object Login : LoginEvent()
    data object ClearError : LoginEvent()
    data object Logout : LoginEvent()
}
