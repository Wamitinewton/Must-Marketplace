package com.example.mustmarket.features.auth.presentation.login.state

import com.example.mustmarket.features.auth.domain.model.AuthedUser

sealed class LoginEvent {

    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class TogglePasswordVisibility(val show: Boolean) : LoginEvent()
    data object Login : LoginEvent()
    data object ClearError : LoginEvent()
}

sealed class LoginUiEvent {
    data class NavigateToHome(val user: AuthedUser) : LoginUiEvent()
}