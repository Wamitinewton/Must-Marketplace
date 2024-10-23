package com.example.mustmarket.features.auth.presentation.splash.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.runtime.*

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.LoopReverseLottieLoader
import com.example.mustmarket.navigation.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(
    navController: NavController
) {
    var animateLogo by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ){
            LoopReverseLottieLoader(
                modifier = Modifier.size(270.dp),
                lottieFile = R.raw.business
            )
            LaunchedEffect(Unit) {
                delay(2000)
                animateLogo = true
                delay(2000)
                navController.popBackStack()
                navController.navigate(Screen.Login.route)
            }

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