package com.example.mustmarket.features.auth.presentation.signup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.UseCases
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.core.coroutine.CoroutineDebugger
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.presentation.signup.event.SignupEvent
import com.example.mustmarket.features.auth.presentation.signup.state.SignUpViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: UseCases
) : ViewModel() {

    private val coroutineDebugger = CoroutineDebugger.getInstance()

    private val _navigateToLogin = Channel<Unit>()
    val navigateToLogin = _navigateToLogin.receiveAsFlow()

    private val _uiEvent = Channel<SignupEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _authUiState: MutableStateFlow<SignUpViewModelState> = MutableStateFlow(
        SignUpViewModelState()
    )
    val authUiState: StateFlow<SignUpViewModelState> get() = _authUiState

    override fun onCleared() {
        super.onCleared()
        coroutineDebugger.cancelAllCoroutines()
        val activeCoroutines = coroutineDebugger.getActiveCoroutinesInfo()
        if (activeCoroutines.isNotEmpty()) {
            Log.d(
                "register",
                "⚠️ Warning: ${activeCoroutines.size} coroutines were still active when ViewModel was cleared:"
            )
            activeCoroutines.forEach { info ->
                Log.d(
                    "register",
                    "📌 Coroutine ${info.id} (${info.tag}) - Running for ${info.duration}ms"
                )
            }
        }
    }

    private fun signupWithEmailAndPassword() {
        val email = _authUiState.value.emailInput
        val password = _authUiState.value.passwordInput
        val passwordConfirm = _authUiState.value.passwordConfirmInput
        val name = _authUiState.value.nameInput

        coroutineDebugger.launchTracked(
            scope = viewModelScope,
            tag = "Signup_process"
        ) {
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || passwordConfirm.isEmpty()) {
                updateSignupState(
                    isLoading = false,
                    errorMessage = "All inputs are required"
                )
                return@launchTracked
            }

            if (password != passwordConfirm) {
                updateSignupState(
                    isLoading = false,
                    errorMessage = "Passwords do not match"
                )
                return@launchTracked
            }
            coroutineDebugger.launchTracked(
                scope = viewModelScope,
                tag = "Signup_NetworkRequest"
            ) {
                authUseCase.authUseCase.registerUseCase(SignUpUser(name, email, password))
                    .collectLatest { result ->
                        when (result) {
                            is Resource.Loading -> updateSignupState(isLoading = true)
                            is Resource.Error -> updateSignupState(
                                errorMessage = result.message ?: "Unknown Error"
                            )

                            is Resource.Success -> {
                                result.data?.let { updateSignupState(result = it) }
                                _navigateToLogin.send(Unit)
                            }
                        }
                    }
            }
        }
    }

    private fun updateSignupState(
        isLoading: Boolean = _authUiState.value.isLoading,
        errorMessage: String = _authUiState.value.errorMessage,
        result: AuthedUser = _authUiState.value.result!!,
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
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "EmailValidation"
                ) {
                    val emailError =
                        if (event.email.isNotEmpty() && !EMAIL_REGEX.matches(event.email)) {
                            "Invalid email format"
                        } else ""
                    _authUiState.value = _authUiState.value.copy(
                        emailInput = event.email,
                        emailError = emailError
                    )
                }
            }

            SignupEvent.ClearError -> {
                updateSignupState(errorMessage = "")

            }

            is SignupEvent.ConfirmPasswordChanged -> {
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "PasswordConfirmValidation"
                ) {
                    val passwordConfirmError =
                        if (event.confirmPassword != _authUiState.value.passwordInput) {
                            "Passwords do not match"
                        } else ""
                    _authUiState.value = _authUiState.value.copy(
                        passwordConfirmInput = event.confirmPassword,
                        passwordConfirmError = passwordConfirmError
                    )
                }

            }

            is SignupEvent.PasswordChanged -> {
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "PasswordValidation"
                ) {
                    val passwordError =
                        if (event.password.isNotEmpty() && !PASSWORD_REGEX.matches(event.password)) {
                            "Invalid password format"
                        } else ""
                    _authUiState.value = _authUiState.value.copy(
                        passwordInput = event.password,
                        passwordError = passwordError
                    )
                }
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
                coroutineDebugger.launchTracked(
                    scope = viewModelScope,
                    tag = "UsernameValidation"
                ) {
                    val nameError = if (event.username.isEmpty()) "Username is required" else ""
                    _authUiState.value = _authUiState.value.copy(
                        nameInput = event.username,
                        nameError = nameError
                    )
                }
            }
        }

    }
}