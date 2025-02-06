package com.newton.mustmarket.features.splash.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.newton.mustmarket.R
import com.newton.mustmarket.core.sharedComposable.LoopReverseLottieLoader
import com.newton.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.newton.mustmarket.navigation.Screen
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var animateLogo by remember { mutableStateOf(false) }
    val isLoggedIn by loginViewModel.isUserLoggedIn.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        delay(2000)
        animateLogo = true
        delay(2000)
        if (isLoggedIn) {
            navController.popBackStack()
            navController.navigate(Screen.HomeScreen.route)
        } else {
            navController.popBackStack()
            navController.navigate(Screen.Onboarding.route)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeUtils.AppColors.Background.themed())
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            LoopReverseLottieLoader(
                modifier = Modifier.size(270.dp),
                lottieFile = R.raw.splash01
            )

            this@Column.AnimatedVisibility(
                visible = animateLogo.not(),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 4000)
                ) + scaleOut(animationSpec = tween(durationMillis = 4000)),
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.78F),
                    painter = painterResource(id = R.drawable.mustmarket),
                    contentDescription = null
                )
            }
        }
    }
}