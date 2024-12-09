package com.example.mustmarket.features.auth.presentation.forgotPassword.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mustmarket.features.auth.presentation.forgotPassword.enums.ForgotPasswordScreen
import com.example.mustmarket.features.auth.presentation.forgotPassword.event.ForgotPasswordEvent
import com.example.mustmarket.features.auth.presentation.forgotPassword.view.otpUtils.PasswordResetSuccess
import com.example.mustmarket.features.auth.presentation.forgotPassword.viewmodels.ForgotPasswordViewModel

@Composable
fun ForgotPasswordRoute(
    onNavigateToLogin: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    when (state.currentScreen) {
        ForgotPasswordScreen.EMAIL_INPUT -> {
            EmailInputScreen(
                email = state.email,
                isLoading = state.isLoading,
                emailError = state.emailError,
                onEmailChanged = { viewModel.onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                onSubmit = { viewModel.onEvent(ForgotPasswordEvent.RequestOtp) },
                onBackPressed = onNavigateToLogin,
                otpError = state.errorMessage
            )
        }

        ForgotPasswordScreen.OTP_INPUT -> {
            OtpVerificationScreen(
                otp = state.otp,
                isLoading = state.isLoading,
                otpError = state.otpError,
                email = state.email,
                onOtpChanged = { viewModel.onEvent(ForgotPasswordEvent.OtpChanged(it)) },
                onResendOtp = { viewModel.onEvent(ForgotPasswordEvent.RequestOtp) },
                onVerifyOtp = { viewModel.onEvent(ForgotPasswordEvent.VerifyOtp) }
            )
        }

        ForgotPasswordScreen.PASSWORD_RESET -> {
            PasswordResetSuccess(onNavigateToLogin = onNavigateToLogin)
        }

        ForgotPasswordScreen.NEW_PASSWORD -> {
            NewPasswordScreen(
                newPassword = state.newPassword,
                confirmPassword = state.confirmPassword,
                isLoading = state.isLoading,
                passwordError = state.passwordError,
                confirmPasswordError = state.confirmPasswordError,
                passwordStrength = state.passwordStrength,
                onPasswordChanged = { viewModel.onEvent(ForgotPasswordEvent.PasswordChanged(it)) },
                onConfirmPasswordChanged = {
                    viewModel.onEvent(
                        ForgotPasswordEvent.ConfirmPasswordChanged(
                            it
                        )
                    )
                },
                onSubmit = { viewModel.onEvent(ForgotPasswordEvent.ResetPassword) }
            )
        }
    }
}