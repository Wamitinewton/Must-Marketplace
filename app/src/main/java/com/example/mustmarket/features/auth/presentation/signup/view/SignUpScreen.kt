package com.example.mustmarket.features.auth.presentation.signup.view

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.core.SharedComposables.ButtonLoading
import com.example.mustmarket.core.SharedComposables.LoopReverseLottieLoader
import com.example.mustmarket.core.SharedComposables.MyTextField
import com.example.mustmarket.core.SharedComposables.NetworkAlertDialog
import com.example.mustmarket.core.SharedComposables.PasswordInput
import com.example.mustmarket.networkManager.NetworkConnectionState
import com.example.mustmarket.networkManager.rememberConnectivityState
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.presentation.signup.event.SignupEvent
import com.example.mustmarket.features.auth.presentation.signup.viewmodels.SignUpViewModel
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by signUpViewModel.authUiState.collectAsStateWithLifecycle()
    val emailIsValid = uiState.emailInput.isNotEmpty() && EMAIL_REGEX.matches(uiState.emailInput)
    val passwordValid =
        uiState.passwordInput.isNotEmpty() && PASSWORD_REGEX.matches(uiState.passwordInput)
    val confirmPasswordValid =
        uiState.passwordConfirmInput.isNotEmpty() && uiState.passwordInput == uiState.passwordConfirmInput
    val userNameValid = uiState.nameInput.isNotEmpty()

    val btnEnabled = emailIsValid && passwordValid && confirmPasswordValid && userNameValid
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()

    val networkState by rememberConnectivityState()
    var showNetworkDialog by remember { mutableStateOf(false) }

    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )

    LaunchedEffect(networkState) {
        showNetworkDialog = networkState == NetworkConnectionState.Unavailable
    }

    if (showNetworkDialog) {
        NetworkAlertDialog(
            onDismiss = { showNetworkDialog = false },
            onExit = { (context as? Activity)?.finish() }
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
        signUpViewModel.navigateToLogin.collect {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.SignUp.route) { inclusive = true }
            }
        }
    }

    fun handleSignupClick() {
        if (networkState == NetworkConnectionState.Available) {
            signUpViewModel.onEvent(SignupEvent.Signup)
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
            text = "Sign up",
            style = MaterialTheme.typography.h2.copy(
                color = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Text(
            text = "Create an account with us to continue",
            style = MaterialTheme.typography.h3,
            color = Color(0xff727272),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        MyTextField(
            onInputChanged = { signUpViewModel.onEvent(SignupEvent.UsernameChanged(it)) },
            inputText = uiState.nameInput,
            name = "Name"
        )

        Spacer(modifier = Modifier.height(20.dp))

        MyTextField(
            onInputChanged = { signUpViewModel.onEvent(SignupEvent.EmailChanged(it)) },
            inputText = uiState.emailInput,
            name = "Email",
            errorMessage = uiState.emailError
        )

        PasswordInput(
            onInputChanged = { signUpViewModel.onEvent(SignupEvent.PasswordChanged(it)) },
            inputText = uiState.passwordInput,
            showPassword = uiState.showPassword,
            toggleShowPassword = {
                signUpViewModel.onEvent(
                    SignupEvent.TogglePasswordVisibility(
                        !uiState.showPassword
                    )
                )
            },
            name = "Password",
            errorMessage = uiState.passwordError
        )

        PasswordInput(
            onInputChanged = {
                signUpViewModel.onEvent(
                    SignupEvent.ConfirmPasswordChanged(
                        it
                    )
                )
            },
            inputText = uiState.passwordConfirmInput,
            showPassword = uiState.showPassword,
            toggleShowPassword = {
                signUpViewModel.onEvent(
                    SignupEvent.ToggleConfirmPasswordVisibility(
                        !uiState.showPassword
                    )
                )
            },
            name = "Confirm Password"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By continuing you agree to our",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "Terms of service",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary
            )
            Text(text = "and ", style = MaterialTheme.typography.h5)
            Text(
                text = "Privacy Policy.",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        ButtonLoading(
            name = "Sign Up",
            isLoading = uiState.isLoading,
            enabled = btnEnabled,
            onClicked = ::handleSignupClick
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

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.h5,
                fontFamily = FontFamily(
                    Font(R.font.gilroysemibold, weight = FontWeight.SemiBold)
                ),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 8.dp)
            )
            IconButton(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Screen.Login.route)
                }
            ) {
                Text(
                    text = "Sign in",
                    style = MaterialTheme.typography.h6,
                    fontFamily = FontFamily(
                        Font(
                            R.font.gilroysemibold,
                            weight = FontWeight.SemiBold
                        )
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}



