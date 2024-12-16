package com.example.mustmarket.features.auth.presentation.login.view

import androidx.compose.foundation.clickable
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
import com.example.mustmarket.core.sharedComposable.DefaultTextInput
import com.example.mustmarket.core.sharedComposable.PasswordInput

@Composable
fun LoginForm(
    emailInput: String,
    passwordInput: String,
    showPassword: Boolean,
    emailError: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePassword: (Boolean) -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefaultTextInput(
            inputText = emailInput,
            onInputChanged = {
                if (!isLoading){
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
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 30.dp)
                .clickable(
                    enabled = !isLoading,
                    onClick = onNavigateToForgotPassword
                ),
            text = "Forgot password",
            style = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.primary

            )
        )
    }
}