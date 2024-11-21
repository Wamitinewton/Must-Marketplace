package com.example.mustmarket.features.auth.presentation.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.LoopReverseLottieLoader

@Composable
fun OtpVerificationScreen(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    onOtpVerify: (String) -> Unit = {},
    onResendOtp: () -> Unit = {},
    navController: NavController
) {
    var canResendOtp by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LoopReverseLottieLoader(
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 30.dp),
            lottieFile = R.raw.success
        )

        Text(
            text = "Enter the OTP sent to your email",
        )
        Spacer(modifier = Modifier.height(16.dp))


        OtpInputField(
            length = otpLength,
            onOtpComplete = onOtpVerify
        )

        Spacer(modifier = Modifier.height(16.dp))

        OtpCountdownTimer(
            onTimerComplete = { canResendOtp = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ResendOtpButton(
            isEnabled = canResendOtp,
            onResendClick = {
                canResendOtp = false
                onResendOtp()
            }
        )
    }
}