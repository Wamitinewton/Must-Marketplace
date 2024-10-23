package com.example.mustmarket.features.auth.presentation.login.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.core.SharedComposables.ButtonLoading
import com.example.mustmarket.core.SharedComposables.MyTextField
import com.example.mustmarket.core.SharedComposables.PasswordInput
import com.example.mustmarket.features.auth.domain.model.LoginUser
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val loginCredentials = LoginUser(
        email = uiState.emailInput,
        password = uiState.passwordInput
    )
    val btnEnabled = uiState.emailInput.isNotEmpty()
            && uiState.passwordInput.isNotEmpty()
    val context = LocalContext.current

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    LaunchedEffect(key1 = uiState.isLoading) {
        if (!uiState.isLoading && uiState.result.isNotEmpty() && uiState.errorMessage.isEmpty()) {
            Toast.makeText(
                context,
                "User ${uiState.result} login successful",
                Toast.LENGTH_SHORT
            ).show()
            navController.popBackStack()
            navController.navigate(Screen.SignUp.route) { launchSingleTop = true }
        } else if (uiState.errorMessage.isNotEmpty()) {
            Toast.makeText(context, "Error ${uiState.errorMessage}", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xfffcfcfc)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 62.dp),
                painter = painterResource(id = R.drawable.ic_min_carrot),
                contentDescription = stringResource(id = R.string.logo)
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 6.dp),
                text = "Enter your email and password",
                style = MaterialTheme.typography.h3,
                color = Color(0xff727272),
                textAlign = TextAlign.Start
            )
            MyTextField(
                onInputChanged = loginViewModel::onEmailInputChanged,
                inputText = uiState.emailInput,
                name = "Email",
                errorMessage = uiState.emailError
            )
            PasswordInput(
                onInputChanged = loginViewModel::onPasswordInputChanged,
                inputText = uiState.passwordInput,
                showPassword = false,
                toggleShowPassword = loginViewModel::toggleShowPassword,
                name = "Password",
                errorMessage = uiState.passwordError
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                text = "Forgot Password",
                style = MaterialTheme.typography.h6
            )
            ButtonLoading(
                name = "Login",
                isLoading = uiState.isLoading,
                enabled = btnEnabled,
                onClicked = {
                    loginViewModel.loginUser(loginCredentials)
                }
            )
            Spacer(modifier = Modifier.height(22.dp))
            Button(
                onClick = {
                    Toast.makeText(context, "Feature not added", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color(0xff5383ec)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign in with Google",
                    style = MaterialTheme.typography.button,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.h6,
                    fontFamily = FontFamily(
                        Font(R.font.gilroysemibold, weight = FontWeight.SemiBold)
                    ),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 8.dp)
                )
                IconButton(
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(Screen.SignUp.route)
                    }
                ) {
                    Text(
                        text = "Sign up",
                        style = MaterialTheme.typography.h6,
                        fontFamily = FontFamily(
                            Font(
                                R.font.gilroysemibold,
                                weight = FontWeight.SemiBold
                            )
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.primary,
                    )
                }
            }

        }
    }
}