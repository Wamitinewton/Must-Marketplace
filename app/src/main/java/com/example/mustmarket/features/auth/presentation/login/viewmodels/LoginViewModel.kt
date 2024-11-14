package com.example.mustmarket.features.auth.presentation.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.datastore.SessionManager
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.state.AuthState
import com.example.mustmarket.features.auth.presentation.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: UseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiEvent = Channel<LoginEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _authUiState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val authUiState: StateFlow<LoginState> get() = _authUiState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        checkSession()
    }

    private fun checkSession() {
        _isLoggedIn.value = sessionManager.isSessionValid()
    }

    private fun logInWithEmailAndPassword() {
        val email = _authUiState.value.emailInput
        val password = _authUiState.value.passwordInput

        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()) {
                _authUiState.value = _authUiState.value.copy(
                    authState = AuthState.LOGGED_OUT,
                    isLoading = false,
                    errorMessage = "All Inputs Are Required"
                )
                return@launch
            }
            val result = authUseCase.authUseCase.loginUseCase(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            result.collectLatest { loginResult ->
                when (loginResult) {
                    is Resource.Loading -> {
                        _authUiState.value = _authUiState.value.copy(
                            authState = AuthState.LOGGED_OUT,
                            isLoading = true,
                            errorMessage = ""
                        )
                    }

                    is Resource.Error -> {
                        _authUiState.value = _authUiState.value.copy(
                            authState = AuthState.LOGGED_OUT,
                            isLoading = false,
                            errorMessage = loginResult.message.toString()
                        )
                    }

                    is Resource.Success -> {
                        _authUiState.value = _authUiState.value.copy(
                            authState = AuthState.AUTHENTICATED,
                            isLoading = false,
                            errorMessage = ""
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                val emailError =
                    if (event.email.isNotEmpty() && !EMAIL_REGEX.matches(event.email)) {
                        "Invalid email format"
                    } else ""
                _authUiState.value = _authUiState.value.copy(
                    emailInput = event.email,
                    emailError = emailError
                )
            }

            LoginEvent.ClearError -> {
                _authUiState.value = _authUiState.value.copy(
                    errorMessage = ""
                )
            }

            LoginEvent.Login -> {
                logInWithEmailAndPassword()
            }

            is LoginEvent.PasswordChanged -> {
                val passwordError =
                    if (event.password.isNotEmpty() && !PASSWORD_REGEX.matches(event.password)) {
                        "Invalid password format"
                    } else ""
                _authUiState.value = _authUiState.value.copy(
                    passwordInput = event.password,
                    passwordError = passwordError
                )
            }

            is LoginEvent.TogglePasswordVisibility -> {
                _authUiState.value = _authUiState.value.copy(
                    showPassword = event.show
                )
            }
        }
    }


}