package com.example.mustmarket.features.auth.presentation.login.view

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mustmarket.core.SharedComposables.MyTextField
import com.example.mustmarket.core.SharedComposables.PasswordInput

@Composable
fun LoginForm(
    emailInput: String,
    passwordInput: String,
    showPassword: Boolean,
    emailError: String,
    passwordError: String? = null,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePassword: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToForgotPassword: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 30.dp)
                .clickable(
                    onClick = onNavigateToForgotPassword
                ),
            text = "Forgot password",
            style = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.primary
            )
        )
    }
}