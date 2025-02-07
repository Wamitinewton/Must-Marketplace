package com.newton.mustmarket.features.auth.presentation.forgotPassword.state

import com.newton.mustmarket.features.auth.presentation.forgotPassword.enums.ForgotPasswordScreen
import com.newton.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength

data class ForgotPasswordState(
    val email: String = "",
    val otp: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isOtpLoading: Boolean = false,
    val isVerificationLoading: Boolean = false,
    val isOtpSent: Boolean = false,
    val isPasswordReset: Boolean = false,
    val currentScreen: ForgotPasswordScreen = ForgotPasswordScreen.EMAIL_INPUT,
    val emailError: String? = null,
    val otpError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val errorMessage: String? = null,
    val passwordStrength: PasswordStrength = PasswordStrength.NONE
)
