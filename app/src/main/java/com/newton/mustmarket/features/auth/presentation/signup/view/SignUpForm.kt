package com.newton.mustmarket.features.auth.presentation.signup.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.newton.mustmarket.core.sharedComposable.DefaultTextInput
import com.newton.mustmarket.core.sharedComposable.PasswordInput
import com.newton.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength

@Composable
fun SignUpForm(
    emailInput: String,
    userNameInput: String,
    passwordInput: String,
    confirmPasswordInput: String,
    showPassword: Boolean,
    showConfirmPassword: Boolean,
    emailError: String,
    userNameError: String,
    passwordError: List<String>,
    confirmPasswordError: List<String>,
    onEmailChanged: (String) -> Unit,
    onUserNameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onTogglePassword: (Boolean) -> Unit,
    onToggleConfirmPassword: (Boolean) -> Unit,
    passwordStrength: PasswordStrength,
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        DefaultTextInput(
            inputText = userNameInput,
            onInputChanged = {
                if (!isLoading) {
                    onUserNameChanged(it)
                }
            },
            name = "Full Name",
            errorMessage = userNameError
        )
        DefaultTextInput(
            inputText = emailInput,
            onInputChanged = {
                if (!isLoading) {
                    onEmailChanged(it)
                }
            },
            name = "Email",
            errorMessage = emailError
        )

        PasswordInput(
            onInputChanged = {
                if (!isLoading) {
                    onPasswordChanged(it)
                }
            },
            inputText = passwordInput,
            showPassword = showPassword,
            toggleShowPassword = onTogglePassword,
            name = "Password",
            errorMessage = passwordError,
            passwordStrength = passwordStrength
        )

        PasswordInput(
            onInputChanged = {
                if (!isLoading) {
                    onConfirmPasswordChanged(it)
                }
            },
            inputText = confirmPasswordInput,
            showPassword = showConfirmPassword,
            toggleShowPassword = onToggleConfirmPassword,
            name = "Confirm Password",
            errorMessage = confirmPasswordError
        )

    }
}