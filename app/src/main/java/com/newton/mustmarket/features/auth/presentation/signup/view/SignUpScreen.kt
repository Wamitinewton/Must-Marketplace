package com.newton.mustmarket.features.auth.presentation.signup.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.newton.mustmarket.R
import com.newton.mustmarket.core.sharedComposable.ButtonLoading
import com.newton.mustmarket.core.sharedComposable.NetworkAlertDialog
import com.newton.mustmarket.core.sharedComposable.SocialAuthButton
import com.newton.mustmarket.core.networkManager.NetworkConnectionState
import com.newton.mustmarket.core.networkManager.rememberConnectivityState
import com.newton.mustmarket.core.util.Constants.EMAIL_REGEX
import com.newton.mustmarket.core.util.Constants.PASSWORD_REGEX
import com.newton.mustmarket.features.auth.presentation.auth_utils.AuthHeader
import com.newton.mustmarket.features.auth.presentation.auth_utils.SignUpPrompt
import com.newton.mustmarket.features.auth.presentation.signup.event.SignupEvent
import com.newton.mustmarket.features.auth.presentation.signup.viewmodels.SignUpViewModel
import com.newton.mustmarket.navigation.Screen
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay


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

    var showSuccessDialog by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

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

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Long
            )
        }
    }



    LaunchedEffect(Unit) {

        signUpViewModel.navigateToLogin.collect {

            showSuccessDialog = true

            delay(4000)

            showSuccessDialog = false

            navController.popBackStack()

            navController.navigate(Screen.Login.route)

        }
    }

    if (showSuccessDialog) {

        SignUpSuccess(

            onDismiss = { showSuccessDialog = false }

        )
    }


    fun handleSignupClick() {

        if (networkState == NetworkConnectionState.Available) {

            signUpViewModel.onEvent(SignupEvent.Signup)

        } else {

            showNetworkDialog = true

        }
    }

   Scaffold(
       scaffoldState = scaffoldState
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
               .verticalScroll(rememberScrollState()),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           AuthHeader(
               authText = "Create an account with us",
               authTitle = "Sign Up"
           )

           SignUpForm(
               emailInput = uiState.emailInput,
               userNameInput = uiState.nameInput,
               passwordInput = uiState.passwordInput,
               confirmPasswordInput = uiState.passwordConfirmInput,
               showPassword = uiState.showPassword,
               showConfirmPassword = uiState.showPassword,
               emailError = uiState.emailError,
               userNameError = uiState.nameError,
               passwordError = uiState.passwordError,
               confirmPasswordError = uiState.passwordConfirmError,
               onEmailChanged = { signUpViewModel.onEvent(SignupEvent.EmailChanged(it)) },
               onUserNameChanged = { signUpViewModel.onEvent(SignupEvent.UsernameChanged(it)) },
               onPasswordChanged = { signUpViewModel.onEvent(SignupEvent.PasswordChanged(it)) },
               onConfirmPasswordChanged = {
                   signUpViewModel.onEvent(
                       SignupEvent.ConfirmPasswordChanged(
                           it
                       )
                   )
               },
               onTogglePassword = {
                   signUpViewModel.onEvent(SignupEvent.TogglePasswordVisibility(!uiState.showPassword))
               },
               passwordStrength = uiState.passwordStrength,
               onToggleConfirmPassword = {
                   signUpViewModel.onEvent(SignupEvent.ToggleConfirmPasswordVisibility(!uiState.showPassword))
               },
               isLoading = uiState.isLoading
           )

           Spacer(modifier = Modifier.height(20.dp))

           TermsAndServices()

           Spacer(modifier = Modifier.height(22.dp))

           ButtonLoading(
               name = "Sign Up",
               isLoading = uiState.isLoading,
               enabled = btnEnabled,
               onClicked = ::handleSignupClick
           )

           Spacer(modifier = Modifier.height(22.dp))

           SocialAuthButton(
               onClick = {},
               iconId = R.drawable.google,
               text = "Continue with Google"
           )

           Spacer(modifier = Modifier.height(22.dp))

           SignUpPrompt(
               onSignUpClick = {
                   navController.popBackStack()
                   navController.navigate(Screen.Login.route)
               },
               authCheck = "Already have an account?",
               authMethod = "Log in",
               isLoading = uiState.isLoading
           )
           Spacer(modifier = Modifier.height(24.dp))
       }
   }
}



