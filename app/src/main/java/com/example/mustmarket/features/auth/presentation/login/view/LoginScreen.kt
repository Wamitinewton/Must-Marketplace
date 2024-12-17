package com.example.mustmarket.features.auth.presentation.login.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
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
import com.example.mustmarket.core.sharedComposable.ButtonLoading
import com.example.mustmarket.core.sharedComposable.NetworkAlertDialog
import com.example.mustmarket.core.sharedComposable.SocialAuthButton
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.features.auth.presentation.auth_utils.AuthHeader
import com.example.mustmarket.features.auth.presentation.auth_utils.SignUpPrompt
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.core.networkManager.NetworkConnectionState
import com.example.mustmarket.core.networkManager.rememberConnectivityState
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
    val scaffoldState = rememberScaffoldState()


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


    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Long
            )
        }
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }



    LaunchedEffect(Unit) {
        loginViewModel.navigateToHome.collect {
            navController.popBackStack()
            navController.navigate(Screen.HomeScreen.route)
        }
    }

    fun handleLoginClick() {
        if (networkState == NetworkConnectionState.Available) {
            loginViewModel.onEvent(LoginEvent.Login)
        } else {
            showNetworkDialog = true
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,

        ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
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
                .verticalScroll(rememberScrollState())
                .imePadding()
                .windowInsetsPadding(
                    insets = androidx.compose.foundation.layout.WindowInsets.navigationBars.union(
                        androidx.compose.foundation.layout.WindowInsets.ime
                    )
                ),
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
                onEmailChanged = { loginViewModel.onEvent(LoginEvent.EmailChanged(it)) },
                onPasswordChanged = { loginViewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                onTogglePassword = {
                    loginViewModel.onEvent(LoginEvent.TogglePasswordVisibility(!uiState.showPassword))
                },
                onNavigateToForgotPassword = {
                    navController.popBackStack()
                    navController.navigate(Screen.Otp.route)
                },
                isLoading = uiState.isLoading
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
                    navController.navigate(Screen.SignUp.route) {
                        popUpTo(Screen.Login.route)

                        launchSingleTop = true
                    }
                },
                authCheck = "Don't have an account?",
                authMethod = "Sign Up",
                isLoading = uiState.isLoading
            )
        }
    }
}
