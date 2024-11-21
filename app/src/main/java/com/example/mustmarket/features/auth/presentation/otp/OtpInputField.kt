package com.example.mustmarket.features.auth.presentation.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    length: Int = 6,
    boxSize: Dp = 50.dp,
    boxSpacing: Dp = 8.dp,
    onOtpComplete: (String) -> Unit = {},
    boxBackgroundColor: Color = ThemeUtils.AppColors.Surface.themed(),
    boxBorderColor: Color = ThemeUtils.AppColors.Divider.themed(),
    textColor: Color = ThemeUtils.AppColors.Text.themed(),
    errorColor: Color = MaterialTheme.colors.error
) {
    var otpValues by remember { mutableStateOf(List(length) { "" }) }
    val focusRequesters = remember { List(length) { FocusRequester() } }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(otpValues) {
        if (otpValues.all { it.isNotEmpty() }) {
            val otpString = otpValues.joinToString("")
            onOtpComplete(otpString)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(boxSpacing)
    ) {
        repeat(length) { index ->
            OtpInputBox(
                value = otpValues[index],
                onValueChange = { newValue ->
                    isError = false

                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        val updatedOtpValues = otpValues.toMutableList()
                        updatedOtpValues[index] = newValue
                        otpValues = updatedOtpValues

                        if (newValue.isNotEmpty() && index < length - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                },
                onBackSpace = {
                    if (otpValues[index].isEmpty() && index > 0) {
                        val updatedOtpValues = otpValues.toMutableList()
                        updatedOtpValues[index - 1] = ""
                        otpValues = updatedOtpValues
                        focusRequesters[index - 1].requestFocus()
                    }
                },
                focusRequester = focusRequesters[index],
                boxSize = boxSize,
                boxBackgroundColor = boxBackgroundColor,
                boxBorderColor = if (isError) errorColor else boxBorderColor,
                textColor = textColor,
                isFirstBox = index == 0
            )
        }
    }
}