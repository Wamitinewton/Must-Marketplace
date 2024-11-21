package com.example.mustmarket.features.auth.presentation.otp

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun ResendOtpButton(
    isEnabled: Boolean = true,
    onResendClick: () -> Unit,
    buttonText: String = "ResendOtp",
    enabledColor: Color = ThemeUtils.AppColors.Primary,
    disabledColor: Color = ThemeUtils.AppColors.SecondaryText.themed()
) {
    val buttonTextStyle = MaterialTheme.typography.button

    Text(
        text = buttonText,
        color = if (isEnabled) enabledColor else disabledColor,
        style = buttonTextStyle,
        modifier = Modifier.clickable(enabled = isEnabled) {
            onResendClick()
        }
    )
}