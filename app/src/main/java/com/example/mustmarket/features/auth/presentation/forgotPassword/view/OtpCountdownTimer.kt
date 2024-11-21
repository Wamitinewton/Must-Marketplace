package com.example.mustmarket.features.auth.presentation.otp

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import kotlinx.coroutines.delay

@Composable
fun OtpCountdownTimer(
    initialTimeInSeconds: Int = 60,
    onTimerComplete: () -> Unit = {},
    timerTextStyle: TextStyle = MaterialTheme.typography.body1,
    activeColor: Color = ThemeUtils.AppColors.Primary,
    inactiveColor: Color = ThemeUtils.AppColors.SecondaryText.themed()

) {
    var remainingTime by remember { mutableIntStateOf(initialTimeInSeconds) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && remainingTime > 0) {
            delay(1000)
            remainingTime--
            if (remainingTime == 0) {
                isTimerRunning = false
                onTimerComplete()
            }
        }
    }

    Text(
        text = "Resend OTP in ${remainingTime}s",
        style = timerTextStyle,
        color = if (isTimerRunning) activeColor else inactiveColor
    )
}