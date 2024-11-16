package com.example.mustmarket.features.auth.presentation.login.view

import android.app.Activity
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.ButtonLoading
import com.example.mustmarket.core.SharedComposables.LoopReverseLottieLoader
import com.example.mustmarket.core.SharedComposables.MyTextField
import com.example.mustmarket.core.SharedComposables.NetworkAlertDialog
import com.example.mustmarket.core.SharedComposables.PasswordInput
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.networkManager.NetworkConnectionState
import com.example.mustmarket.networkManager.rememberConnectivityState
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.ThemeUtils.themedColor
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
        LoopReverseLottieLoader(
            lottieFile = R.raw.welcome,
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "Log In",
            style = MaterialTheme.typography.h2.copy(
                color = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Enter your credentials to continue",
            style = MaterialTheme.typography.h3,
            color = Color(0xff727272),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        MyTextField(
            inputText = uiState.emailInput,
            onInputChanged = { loginViewModel.onEvent(LoginEvent.EmailChanged(it)) },
            name = "Email",
            errorMessage = uiState.emailError
        )
        Spacer(modifier = Modifier.height(20.dp))

        PasswordInput(
            onInputChanged = { loginViewModel.onEvent(LoginEvent.PasswordChanged(it)) },
            inputText = uiState.passwordInput,
            showPassword = uiState.showPassword,
            toggleShowPassword = {
                loginViewModel.onEvent(
                    LoginEvent.TogglePasswordVisibility(
                        !uiState.showPassword
                    )
                )
            },
            name = "Password",
            errorMessage = uiState.passwordError
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 22.dp),
            text = "Forgot Password",
            style = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.primary
            )
        )

        ButtonLoading(
            name = "Login",
            isLoading = uiState.isLoading,
            enabled = btnEnabled,
            onClicked = ::handleLoginClick
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
                style = MaterialTheme.typography.h6.copy(
                    color = Color(0xff727272),
                ),
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
