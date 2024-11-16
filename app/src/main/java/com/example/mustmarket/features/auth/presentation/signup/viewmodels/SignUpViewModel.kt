package com.example.mustmarket.features.auth.presentation.signup.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.presentation.signup.event.SignupEvent
import com.example.mustmarket.features.auth.presentation.signup.state.SignUpViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: UseCases
) : ViewModel() {

    private val _navigateToLogin = Channel<Unit>()
    val navigateToLogin = _navigateToLogin.receiveAsFlow()

    private val _uiEvent = Channel<SignupEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _authUiState: MutableStateFlow<SignUpViewModelState> = MutableStateFlow(
        SignUpViewModelState()
    )
    val authUiState: StateFlow<SignUpViewModelState> get() = _authUiState


    private fun signupWithEmailAndPassword() {
        val email = _authUiState.value.emailInput
        val password = _authUiState.value.passwordInput
        val passwordConfirm = _authUiState.value.passwordConfirmInput
        val name = _authUiState.value.nameInput

        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || passwordConfirm.isEmpty()) {
                updateSignupState(
                    isLoading = false,
                    errorMessage = "All inputs are required"
                )
                return@launch
            }

            if (password != passwordConfirm) {
                updateSignupState(
                    isLoading = false,
                    errorMessage = "Passwords do not match"
                )
                return@launch
            }
            authUseCase.authUseCase.registerUseCase(SignUpUser(name, email, password))
                .collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> updateSignupState(isLoading = true)
                        is Resource.Error -> updateSignupState(
                            errorMessage = result.message ?: "Unknown Error"
                        )

                        is Resource.Success -> {
                            updateSignupState(result = "Success")
                            _navigateToLogin.send(Unit)
                        }
                    }
                }
        }
    }

    private fun updateSignupState(
        isLoading: Boolean = _authUiState.value.isLoading,
        errorMessage: String = _authUiState.value.errorMessage,
        result: String = _authUiState.value.result,
    ) {
        _authUiState.value = _authUiState.value.copy(
            isLoading = isLoading,
            errorMessage = errorMessage,
            result = result
        )
    }

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.EmailChanged -> {
                val emailError =
                    if (event.email.isNotEmpty() && !EMAIL_REGEX.matches(event.email)) {
                        "Invalid email format"
                    } else ""
                _authUiState.value = _authUiState.value.copy(
                    emailInput = event.email,
                    emailError = emailError
                )
            }

            SignupEvent.ClearError -> {
                updateSignupState(errorMessage = "")

            }

            is SignupEvent.ConfirmPasswordChanged -> {
                val passwordConfirmError =
                    if (event.confirmPassword != _authUiState.value.passwordInput) {
                        "Passwords do not match"
                    } else ""
                _authUiState.value = _authUiState.value.copy(
                    passwordConfirmInput = event.confirmPassword,
                    passwordConfirmError = passwordConfirmError
                )

            }

            is SignupEvent.PasswordChanged -> {
                val passwordError =
                    if (event.password.isNotEmpty() && !PASSWORD_REGEX.matches(event.password)) {
                        "Invalid password format"
                    } else ""
                _authUiState.value = _authUiState.value.copy(
                    passwordInput = event.password,
                    passwordError = passwordError
                )
            }

            SignupEvent.Signup -> signupWithEmailAndPassword()
            is SignupEvent.ToggleConfirmPasswordVisibility -> {
                _authUiState.value = _authUiState.value.copy(
                    showPassword = event.show
                )
            }

            is SignupEvent.TogglePasswordVisibility -> {
                _authUiState.value = _authUiState.value.copy(
                    showPassword = event.show
                )
            }

            is SignupEvent.UsernameChanged -> {
                val nameError = if (event.username.isEmpty()) "Username is required" else ""
                _authUiState.value = _authUiState.value.copy(
                    nameInput = event.username,
                    nameError = nameError
                )
            }
        }

    }
}