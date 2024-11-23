package com.example.mustmarket.features.auth.presentation.signup.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mustmarket.core.SharedComposables.MyTextField
import com.example.mustmarket.core.SharedComposables.PasswordInput
import com.example.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength

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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        MyTextField(
            inputText = userNameInput,
            onInputChanged = onUserNameChanged,
            name = "Full Name",
            errorMessage = userNameError
        )
        MyTextField(
            inputText = emailInput,
            onInputChanged = onEmailChanged,
            name = "Email",
            errorMessage = emailError
        )

        PasswordInput(
            onInputChanged = onPasswordChanged,
            inputText = passwordInput,
            showPassword = showPassword,
            toggleShowPassword = onTogglePassword,
            name = "Password",
            errorMessage = passwordError,
            passwordStrength = passwordStrength
        )

        PasswordInput(
            onInputChanged = onConfirmPasswordChanged,
            inputText = confirmPasswordInput,
            showPassword = showConfirmPassword,
            toggleShowPassword = onToggleConfirmPassword,
            name = "Confirm Password",
            errorMessage = confirmPasswordError
        )

    }
}