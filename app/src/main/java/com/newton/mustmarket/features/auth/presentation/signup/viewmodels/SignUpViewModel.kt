package com.newton.mustmarket.features.auth.presentation.signup.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newton.mustmarket.core.util.Constants.EMAIL_REGEX
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.auth.domain.model.SignUpUser
import com.newton.mustmarket.features.auth.presentation.auth_utils.PwdValidators.SCORE_THRESHOLDS
import com.newton.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength
import com.newton.mustmarket.features.auth.presentation.signup.event.SignupEvent
import com.newton.mustmarket.features.auth.presentation.signup.state.SignUpViewModelState
import com.newton.mustmarket.usecase.UseCases
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
            when {
                email.isEmpty() || password.isEmpty() || name.isEmpty() || passwordConfirm.isEmpty() -> {
                    updateSignupState(
                        isLoading = false,
                        errorMessage = "All inputs are required"
                    )
                    return@launch
                }

                !validatePassword(password) -> {
                    val errors = buildPasswordErrorList(password)
                    _authUiState.value = _authUiState.value.copy(
                        isLoading = false,
                        passwordError = errors
                    )
                    return@launch
                }

                password != passwordConfirm -> {
                    _authUiState.value = _authUiState.value.copy(
                        isLoading = false,
                        passwordConfirmError = listOf("Passwords do not match")
                    )
                    return@launch
                }
            }

                authUseCase.authUseCase.registerUseCase(SignUpUser(name, email, password))
                    .collectLatest { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _authUiState.value = _authUiState.value.copy(
                                    isLoading = true,
                                    errorMessage = null
                                )
                            }

                            is Resource.Error -> {
                                _authUiState.value = _authUiState.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: "Unknown error occurred"
                                )
                            }

                            is Resource.Success -> {
                                _authUiState.value = _authUiState.value.copy(
                                    isLoading = false,
                                    errorMessage = null,
                                    result = result.data!!
                                )
                                _navigateToLogin.send(Unit)
                            }
                        }
                    }
            }
        }

    private fun updateSignupState(
        isLoading: Boolean = _authUiState.value.isLoading,
        errorMessage: String = _authUiState.value.errorMessage ?: "Unknown error occurred",
        result: AuthedUser = _authUiState.value.result,
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
                    val passwordConfirmError = mutableListOf<String>()
                    if (event.confirmPassword.isNotEmpty() && event.confirmPassword != _authUiState.value.passwordInput) {
                        passwordConfirmError.add("Passwords do not match")
                    }
                    _authUiState.value = _authUiState.value.copy(
                        passwordConfirmInput = event.confirmPassword,
                        passwordConfirmError = passwordConfirmError
                    )
                }


            is SignupEvent.PasswordChanged -> {
                    val password = event.password
                    _authUiState.value = _authUiState.value.copy(
                        passwordInput = password,
                        passwordError = if (password.isNotEmpty()) buildPasswordErrorList(password) else emptyList(),
                        passwordStrength = getPasswordStrength(password)
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


    private fun validatePassword(password: String): Boolean {
        return password.matches(".*[0-9].*".toRegex()) &&
                password.matches(".*[a-z].*".toRegex()) &&
                password.matches(".*[A-Z].*".toRegex()) &&
                password.matches(".*[@#$%^_&+=].*".toRegex()) &&
                !password.contains(" ") &&
                password.length >= 8
    }

    private fun buildPasswordErrorList(password: String): List<String> {
        return buildList {
            if (!password.matches(".*[0-9].*".toRegex())) add("Password must contain at least one digit")
            if (!password.matches(".*[a-z].*".toRegex())) add("Password must contain at least one lowercase letter")
            if (!password.matches(".*[A-Z].*".toRegex())) add("Password must contain at least one uppercase letter")
            if (!password.matches(".*[@#$%^_&+=].*".toRegex())) add("Password must contain at least one special character (@#\$%^_&+=)")
            if (password.contains(" ")) add("Password must not contain whitespace")
            if (password.length < 8) add("Password must be at least 8 characters long")
        }
    }


    private fun getPasswordStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.NONE

        var score = 0
        if (password.matches(".*[0-9].*".toRegex())) score++
        if (password.matches(".*[a-z].*".toRegex())) score++
        if (password.matches(".*[A-Z].*".toRegex())) score++
        if (password.matches(".*[@#$%^_&+=].*".toRegex())) score++
        if (password.length >= 8) score++
        if (password.length >= 12) score++

        return SCORE_THRESHOLDS.entries.firstOrNull {
            score in it.key
        }?.value ?: PasswordStrength.NONE
    }
}