package com.example.mustmarket.features.auth.presentation.forgotPassword.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mustmarket.core.SharedComposables.LoadingState

@Composable
fun EmailInputScreen(
    email: String,
    isLoading: Boolean,
    emailError: String?,
    onEmailChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onBackPressed: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "navigate back"
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Forgot password",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Enter your email address to receive a verification code",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChanged,
                label = { Text("Email") },
                isError = emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onSubmit()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            AnimatedVisibility(visible = emailError != null) {
                Text(
                    text = emailError ?: "",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(top = 4.dp)
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
                    Text(
                        text = "Send code"
                    )
                }
            }
        }
    }
}