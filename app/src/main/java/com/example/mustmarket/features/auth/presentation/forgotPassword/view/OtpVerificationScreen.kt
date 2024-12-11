package com.example.mustmarket.features.auth.presentation.forgotPassword.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mustmarket.core.AdaptableLayout.ResponsiveSizeUtil
import com.example.mustmarket.core.SharedComposables.LoadingState
import com.example.mustmarket.features.auth.presentation.forgotPassword.view.otpUtils.OtpBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpVerificationScreen(
    otp: String,
    isLoading: Boolean,
    otpError: String?,
    email: String,
    onOtpChanged: (String) -> Unit,
    onResendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    resendOtpError: String?,
    onBackPressed: () -> Unit
) {
    var remainingSeconds by remember { mutableIntStateOf(60) }
    var isTimerRunning by remember { mutableStateOf(true) }
    val focusRequester = remember { List(6) { FocusRequester() } }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = isTimerRunning) {
        while (isTimerRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
            if (remainingSeconds == 0) {
                isTimerRunning = false
            }
        }
    }

    LaunchedEffect(resendOtpError) {
        resendOtpError?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Long
            )
        }
    }

   Scaffold(
       scaffoldState = scaffoldState,
       topBar = {
           TopAppBar(
               title = { },
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
   ) { padding ->
       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(padding)
               .padding(ResponsiveSizeUtil.responsivePadding(basePadding = 16.dp)),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           Text(
               text = "Enter verification code",
               fontSize = ResponsiveSizeUtil.responsiveTextSize(baseSize = 24.sp),
               fontWeight = FontWeight.Bold,
               modifier = Modifier.padding(bottom = ResponsiveSizeUtil.responsivePadding(basePadding = 18.dp))
           )

           Text(
               text = "We've sent a verification code to\n$email",
               fontSize = ResponsiveSizeUtil.responsiveTextSize(baseSize = 19.sp),
               textAlign = TextAlign.Center,
               modifier = Modifier.padding(bottom = ResponsiveSizeUtil.responsivePadding(basePadding = 27.dp))
           )

           Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceEvenly
           ) {
               otp.padEnd(6, ' ').forEachIndexed { index, char ->
                   val isCurrentBoxFilled = if (index == 0) true else otp.getOrNull(index - 1)?.isDigit() == true

                   OtpBox(
                       value = (char.takeIf { it != ' ' } ?: "").toString(),
                       isFocused = index == otp.length,
                       isEnabled = isCurrentBoxFilled,
                       focusRequester = focusRequester[index],
                       onValueChanged = { newValue ->
                           if (newValue.isEmpty()) {
                               if (index > 0) {
                                   onOtpChanged(otp.take(index - 1) + otp.drop(index))
                                   coroutineScope.launch {
                                       focusRequester[index - 1].requestFocus()
                                   }
                               } else {
                                   onOtpChanged(otp.drop(1))
                               }
                           } else if (newValue.matches(Regex("\\d"))) {
                               val newOtp = otp.take(index) + newValue + otp.drop(index + 1)
                               onOtpChanged(newOtp)
                               if (index < 5) {
                                   coroutineScope.launch {
                                       focusRequester[index + 1].requestFocus()
                                   }
                               }
                           }
                       }
                   )
               }
           }

           AnimatedVisibility(visible = otpError != null) {
               Text(
                   text = otpError ?: "",
                   color = MaterialTheme.colors.error,
                   modifier = Modifier.padding(top = ResponsiveSizeUtil.responsivePadding(basePadding = 16.dp))
               )
           }
           Button(
               onClick = onVerifyOtp,
               enabled = !isLoading && otp.length == 6,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(
                       horizontal = ResponsiveSizeUtil.responsivePadding(basePadding = 16.dp),
                       vertical = ResponsiveSizeUtil.responsivePadding(basePadding = 24.dp)
                   )
                   .height(ResponsiveSizeUtil.responsiveDimension(baseDimension = 50.dp))
           ) {
               if (isLoading) {
                   LoadingState()
               } else {
                   Text("verify",
                       fontSize = ResponsiveSizeUtil.responsiveTextSize(baseSize = 16.sp)
                   )
               }
           }
           Row(
               modifier = Modifier.padding(top = ResponsiveSizeUtil.responsivePadding(basePadding = 16.dp)),
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(
                   "Didn't receive the code?",
                   fontSize = ResponsiveSizeUtil.responsiveTextSize(baseSize = 17.sp)
               )
               if (remainingSeconds > 0) {
                   Text(
                       text = "Wait ${remainingSeconds}s",
                       color = MaterialTheme.colors.primary,
                       fontSize = ResponsiveSizeUtil.responsiveTextSize(baseSize = 17.sp)
                   )
               } else {
                   Text(
                       text = "Resend",
                       color = MaterialTheme.colors.primary,
                       modifier = Modifier.clickable {
                           onResendOtp()
                           remainingSeconds = 60
                           isTimerRunning = true
                       },
                       fontSize = ResponsiveSizeUtil.responsiveTextSize(baseSize = 17.sp,)
                   )
               }
           }
       }
   }
}