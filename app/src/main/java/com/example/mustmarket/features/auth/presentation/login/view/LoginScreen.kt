package com.example.mustmarket.features.auth.presentation.login.view

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.ButtonLoading
import com.example.mustmarket.core.SharedComposables.NetworkAlertDialog
import com.example.mustmarket.core.SharedComposables.SocialAuthButton
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.features.auth.presentation.auth_utils.AuthHeader
import com.example.mustmarket.features.auth.presentation.auth_utils.SignUpPrompt
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.networkManager.NetworkConnectionState
import com.example.mustmarket.networkManager.rememberConnectivityState
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by loginViewModel.authUiState.collectAsStateWithLifecycle()
    val emailIsValid = uiState.emailInput.isNotEmpty() && EMAIL_REGEX.matches(uiState.emailInput)
    val passwordIsValid =
        uiState.passwordInput.isNotEmpty() && PASSWORD_REGEX.matches(uiState.passwordInput)
    val btnEnabled = emailIsValid && passwordIsValid
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()


    val networkState by rememberConnectivityState()
    var showNetworkDialog by remember { mutableStateOf(false) }

    LaunchedEffect(networkState) {
        showNetworkDialog = networkState == NetworkConnectionState.Unavailable
    }

    if (showNetworkDialog) {
        NetworkAlertDialog(
            onDismiss = { showNetworkDialog = false },
            onExit = { (context as? Activity)?.finish() }
        )
    }


    LaunchedEffect(key1 = uiState.errorMessage) {
        if (uiState.errorMessage.isNotEmpty()) {
            loginViewModel.onEvent(LoginEvent.ClearError)
        }
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    LaunchedEffect(key1 = uiState.isLoading) {
        if (!uiState.isLoading && uiState.result.isNotEmpty() && uiState.errorMessage.isEmpty()) {
            if (networkState == NetworkConnectionState.Available) {
                Toast.makeText(
                    context,
                    "User ${uiState.result} login successful",
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
                navController.navigate(Screen.SignUp.route) { launchSingleTop = true }
            } else {
                showNetworkDialog = true
            }
        } else if (uiState.errorMessage.isNotEmpty()) {
            Toast.makeText(context, "Error ${uiState.errorMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        loginViewModel.navigateToHome.collect {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    fun handleLoginClick() {
        if (networkState == NetworkConnectionState.Available) {
            loginViewModel.onEvent(LoginEvent.Login)
        } else {
            showNetworkDialog = true
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ThemeUtils.AppColors.Surface.themed(),
                        ThemeUtils.AppColors.Surface.themed(),
                        ThemeUtils.AppColors.Surface.themed(),
                    )
                )
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AuthHeader(
            authText = "Enter your credentials to continue",
            authTitle = "Log In"
        )

        LoginForm(
            emailInput = uiState.emailInput,
            passwordInput = uiState.passwordInput,
            showPassword = uiState.showPassword,
            emailError = uiState.emailError,
            passwordError = uiState.passwordError,
            onEmailChanged = { loginViewModel.onEvent(LoginEvent.EmailChanged(it)) },
            onPasswordChanged = { loginViewModel.onEvent(LoginEvent.PasswordChanged(it)) },
            onTogglePassword = {
                loginViewModel.onEvent(LoginEvent.TogglePasswordVisibility(!uiState.showPassword))
            }
        )

        ButtonLoading(
            name = "Login",
            isLoading = uiState.isLoading,
            enabled = btnEnabled,
            onClicked = ::handleLoginClick
        )

        Spacer(modifier = Modifier.height(22.dp))

        SocialAuthButton(
            onClick = {},
            iconId = R.drawable.google,
            text = "Continue with Google"
        )

        Spacer(modifier = Modifier.height(12.dp))

        SignUpPrompt(
            onSignUpClick = {
                navController.popBackStack()
                navController.navigate(Screen.SignUp.route)
            },
            authCheck = "Don't have an account?",
            authMethod = "Sign Up"
        )
    }
}
