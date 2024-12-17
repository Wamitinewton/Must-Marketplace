package com.example.mustmarket.features.auth.presentation.forgotPassword.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mustmarket.core.sharedComposable.LoadingState
import com.example.mustmarket.core.sharedComposable.PasswordStrengthIndicator
import com.example.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength

@Composable
fun NewPasswordScreen(
    newPassword: String,
    confirmPassword: String,
    isLoading: Boolean,
    passwordError: String?,
    confirmPasswordError: String?,
    passwordStrength: PasswordStrength,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create New Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Please create a strong password for your account",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = onPasswordChanged,
            label = { Text("New Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            isError = passwordError != null,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        AnimatedVisibility(visible = passwordError != null) {
            Text(
                text = passwordError ?: "",
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        when (passwordStrength) {
            PasswordStrength.WEAK -> {
                PasswordStrengthIndicator(
                    strength = "Weak",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            PasswordStrength.MEDIUM -> {
                PasswordStrengthIndicator(
                    strength = "Medium",
                    color = Color.Yellow,
                    modifier = Modifier.padding(16.dp)
                )
            }

            PasswordStrength.STRONG -> {
                PasswordStrengthIndicator(
                    strength = "Strong",
                    color = Color.Green,
                    modifier = Modifier.padding(16.dp)
                )
            }

            PasswordStrength.VERY_STRONG -> {
                PasswordStrengthIndicator(
                    strength = "Very Strong",
                    color = Color.Green,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {}
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            isError = confirmPasswordError != null,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        AnimatedVisibility(visible = confirmPasswordError != null) {
            Text(
                text = confirmPasswordError ?: "",
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Button(
            onClick = onSubmit,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(50.dp)
        ) {
            if (isLoading) {
                LoadingState()
            } else {
                Text("Reset password")
            }
        }
    }
}