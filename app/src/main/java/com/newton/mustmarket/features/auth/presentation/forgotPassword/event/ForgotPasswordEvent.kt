package com.newton.mustmarket.features.auth.presentation.forgotPassword.event

sealed class ForgotPasswordEvent {
    data class EmailChanged(val email: String) : ForgotPasswordEvent()
    data class OtpChanged(val otp: String) : ForgotPasswordEvent()
    data class PasswordChanged(val password: String) : ForgotPasswordEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : ForgotPasswordEvent()
    data object RequestOtp : ForgotPasswordEvent()
    data object ResetPassword : ForgotPasswordEvent()
    data object ClearErrors : ForgotPasswordEvent()
    data object VerifyOtp : ForgotPasswordEvent()
}