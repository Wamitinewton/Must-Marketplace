package com.example.mustmarket.features.auth.presentation.login.view

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material.Card
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
import com.example.mustmarket.core.SharedComposables.ButtonLoading
import com.example.mustmarket.core.SharedComposables.MyTextField
import com.example.mustmarket.core.SharedComposables.NetworkAlertDialog
import com.example.mustmarket.core.SharedComposables.PasswordInput
import com.example.mustmarket.core.networkManager.NetworkConnectionState
import com.example.mustmarket.core.networkManager.rememberConnectivityState
import com.example.mustmarket.core.util.Constants.EMAIL_REGEX
import com.example.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.ThemeUtils.themedColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }
    val rotation = remember { Animatable(0f) }

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

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                rotation.animateTo(
                    targetValue = 720f,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
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


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5E9),
                        Color(0xFFC8E6C9),
                        Color(0xFFA5D6A7)
                    )
                )
            )
    ) {
        val (logo, welcomeText, card) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(parent.top, 32.dp)
                    centerHorizontallyTo(parent)
                },
            painter = painterResource(id = R.drawable.ic_min_carrot),
            contentDescription = stringResource(id = R.string.logo)
        )

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.colors.primary,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .alpha(alpha.value)
                .graphicsLayer(
                    rotationZ = rotation.value,
                    scaleX = scale.value,
                    scaleY = scale.value
                )
                .constrainAs(welcomeText) {
                    top.linkTo(logo.bottom, 40.dp)
                    centerHorizontallyTo(parent)
                }
        )

        Card(
            modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(welcomeText.bottom, 200.dp)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            backgroundColor = ThemeUtils.AppColors.Background.themed(),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.h2.copy(
                        color = ThemeUtils.AppColors.Teal200,
                        fontSize = 26.sp
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 6.dp),
                    text = "Enter your email and password",
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.themedColor(ThemeUtils.AppColors.SecondaryText),
                    textAlign = TextAlign.Start
                )

                MyTextField(
                    inputText = uiState.emailInput,
                    onInputChanged = { loginViewModel.onEvent(LoginEvent.EmailChanged(it)) },
                    name = "Email",
                    errorMessage = uiState.emailError
                )

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
                        .padding(vertical = 10.dp),
                    text = "Forgot Password",
                    style = MaterialTheme.typography.h6.copy(
                        color = ThemeUtils.AppColors.Teal200
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
    }
}