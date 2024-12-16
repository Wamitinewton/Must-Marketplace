package com.example.mustmarket.features.auth.presentation.forgotPassword.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.usecase.UseCases
import com.example.mustmarket.core.coroutineLogger.CoroutineDebugger
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.presentation.auth_utils.PwdValidators.OTP_LENGTH
import com.example.mustmarket.features.auth.presentation.auth_utils.PwdValidators.SCORE_THRESHOLDS
import com.example.mustmarket.features.auth.presentation.forgotPassword.enums.ForgotPasswordScreen
import com.example.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength
import com.example.mustmarket.features.auth.presentation.forgotPassword.event.ForgotPasswordEvent
import com.example.mustmarket.features.auth.presentation.forgotPassword.state.ForgotPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authUseCase: UseCases,
) : ViewModel() {
    private val coroutineDebugger = CoroutineDebugger.getInstance()

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()


    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            ForgotPasswordEvent.ClearErrors -> {
                _state.update {
                    it.copy(
                        emailError = null,
                        otpError = null,
                        passwordError = null,
                        confirmPasswordError = null,
                        errorMessage = null,
                        passwordStrength = getPasswordStrengthMessage(it.newPassword)
                    )
                }
            }

            is ForgotPasswordEvent.ConfirmPasswordChanged -> {
                _state.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        confirmPasswordError = null
                    )
                }
            }

            is ForgotPasswordEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, emailError = null) }
            }

            is ForgotPasswordEvent.OtpChanged -> {
                _state.update { it.copy(otp = event.otp, otpError = null) }
            }

            is ForgotPasswordEvent.PasswordChanged -> {
                _state.update {
                    it.copy(
                        newPassword = event.password,
                        passwordError = null,
                        passwordStrength = getPasswordStrengthMessage(event.password)
                    )
                }
            }

            ForgotPasswordEvent.RequestOtp -> {
                requestOtp()
            }

            ForgotPasswordEvent.ResetPassword -> {
                resetPassword()
            }

            ForgotPasswordEvent.VerifyOtp -> {
                verifyOtp()
            }
        }
    }

    private fun verifyOtp() {
        if (_state.value.otp.length != OTP_LENGTH) {
            _state.update { it.copy(otpError = "Please enter a valid $OTP_LENGTH-digit OTP") }
            return
        }
        _state.update {
            it.copy(
                currentScreen = ForgotPasswordScreen.NEW_PASSWORD,
                otpError = null
            )
        }
    }

    private fun requestOtp() {
        if (!validateEmail()) return

        coroutineDebugger.launchTracked(
            tag = "request-Otp",
            scope = viewModelScope
        ) {
            authUseCase.authUseCase.requestOtpUseCase(
                RequestPasswordReset(
                    email = _state.value.email
                )
            )
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.update { it.copy(
                                isOtpLoading = true,
                                isOtpSent = false,
                                errorMessage = null
                            ) }
                        }

                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    errorMessage = result.message,
                                    isOtpLoading = false,
                                    isOtpSent = false
                                )
                            }
                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isOtpSent = true,
                                    currentScreen = ForgotPasswordScreen.OTP_INPUT,
                                    isOtpLoading = false,
                                    errorMessage = null
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun resetPassword() {
        if (!validateResetPasswordInput()) return
        coroutineDebugger.launchTracked(
            tag = "reset-Password",
            scope = viewModelScope,
        ) {
            val otpRequest = OtpRequest(
                email = _state.value.email,
                otp = _state.value.otp,
                newPassword = _state.value.newPassword
            )
            authUseCase.authUseCase.resetPasswordUseCase(otpRequest)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.update { it.copy(isVerificationLoading = result.isLoading) }
                        }

                        is Resource.Error -> {
                            _state.update {
                                it.copy(errorMessage = result.message)
                            }
                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isPasswordReset = true,
                                    currentScreen = ForgotPasswordScreen.PASSWORD_RESET
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun validateEmail(): Boolean {
        return when {
            _state.value.email.isEmpty() -> {
                _state.update { it.copy(emailError = "Email cannot be empty") }
                false
            }

            !_state.value.email.matches(EMAIL_REGEX) -> {
                _state.update { it.copy(emailError = "Please enter a valid email address") }
                false
            }

            else -> true
        }
    }

    private fun validateResetPasswordInput(): Boolean {
        var isValid = true
        val currentState = _state.value

        if (currentState.otp.isEmpty()) {
            _state.update { it.copy(otpError = "OTP cannot be empty") }
            isValid = false
        } else if (currentState.otp.length != 6) {
            _state.update { it.copy(otpError = "OTP must be 6 digits") }
            isValid = false
        }

        if (currentState.newPassword.isEmpty()) {
            _state.update { it.copy(passwordError = "Password cannot be empty") }
            isValid = false
        } else if (!currentState.newPassword.matches(PASSWORD_REGEX)) {
            _state.update {
                it.copy(passwordError = buildPasswordErrorMessage(currentState.newPassword))
            }
            isValid = false
        }

        if (currentState.confirmPassword.isEmpty()) {
            _state.update { it.copy(confirmPasswordError = "Please confirm your password") }
            isValid = false
        } else if (currentState.newPassword != currentState.confirmPassword) {
            _state.update { it.copy(confirmPasswordError = "Password do not match") }
            isValid = false
        }

        return isValid
    }

    private fun buildPasswordErrorMessage(password: String): String {
        val requirements = mutableListOf<String>()

        if (!password.matches(".*[0-9].*".toRegex()))
            requirements.add("at least one digit")
        if (!password.matches(".*[a-z].*".toRegex()))
            requirements.add("at least one lowercase letter")
        if (!password.matches(".*[A-Z].*".toRegex()))
            requirements.add("at least one uppercase letter")
        if (!password.matches(".*[@#$%^_&+=].*".toRegex()))
            requirements.add("at least one special character (@#\$%^_&+=)")
        if (password.contains(" "))
            requirements.add("no whitespace")
        if (password.length < 8)
            requirements.add("minimum 8 characters")

        return if (requirements.isEmpty()) {
            "Invalid password format"
        } else {
            "Password must contain " + requirements.joinToString(", ")
        }
    }


    private fun getPasswordStrengthMessage(password: String): PasswordStrength {
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