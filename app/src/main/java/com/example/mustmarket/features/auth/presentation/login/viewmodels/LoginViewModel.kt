package com.example.mustmarket.features.auth.presentation.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.core.util.LoadingState
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.FinalUser
import com.example.mustmarket.features.auth.domain.model.LoginUser
import com.example.mustmarket.features.auth.presentation.login.state.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.state.LoginUiEvent
import com.example.mustmarket.features.auth.presentation.login.state.LoginViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: UseCases
) : ViewModel() {

    private val viewModelState = MutableStateFlow(LoginViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val loadingState = MutableStateFlow(LoadingState.IDLE)

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> onEmailInputChanged(event.email)
            is LoginEvent.PasswordChanged -> onPasswordInputChanged(event.password)
            is LoginEvent.TogglePasswordVisibility -> toggleShowPassword(event.show)
            LoginEvent.Login -> validateAndLogin()
            LoginEvent.ClearError -> clearError()
        }
    }

    private fun validateAndLogin() {
        val currentState = viewModelState.value

        if (currentState.emailError.isNotEmpty() || currentState.passwordError.isNotEmpty()) {
            return
        }
        if (currentState.emailInput.isEmpty() || currentState.passwordInput.isEmpty()) {
            viewModelState.update {
                it.copy(
                    errorMessage = "Please fill all fields"
                )
            }
            return
        }
        loginUser(
            LoginUser(
                email = currentState.emailInput.trim(),
                password = currentState.passwordInput
            )
        )
    }

    private fun loginUser(loginCredentials: LoginUser) {
        viewModelScope.launch {
            authUseCase.authUseCase.loginUseCase(loginCredentials).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { loginResult ->
                            viewModelState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    result = loginResult.user.name,
                                    errorMessage = ""
                                )
                            }

                            _uiEvent.send(LoginUiEvent.NavigateToHome(loginResult.user))
                        }
                    }

                    is Resource.Error -> {
                        viewModelState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "An excepted error occurred",
                                result = ""
                            )
                        }
                        loadingState.value = LoadingState.Loading
                    }

                    is Resource.Loading -> {
                        viewModelState.update { it.copy(isLoading = true) }
                        loadingState.value = LoadingState.Loading
                    }
                }
            }
        }
    }

    private fun onEmailInputChanged(emailInput: String) {
        val emailError = if (emailInput.isNotEmpty() && !EMAIL_REGEX.matches(emailInput)) {
            "Invalid email format"
        } else ""

        viewModelState.update {
            it.copy(
                emailInput = emailInput,
                emailError = emailError
            )
        }
    }

    private fun onPasswordInputChanged(password: String) {
        val passwordError = if (password.isNotEmpty() && !PASSWORD_REGEX.matches(password)) {
            "Invalid password format"
        } else ""

        viewModelState.update {
            it.copy(
                passwordInput = password,
                passwordError = passwordError
            )
        }
    }

    private fun toggleShowPassword(show: Boolean) {
        viewModelState.update {
            it.copy(showPassword = show)
        }
    }

    private fun clearError() {
        viewModelState.update {
            it.copy(errorMessage = "")
        }
        loadingState.value = LoadingState.IDLE
    }

}