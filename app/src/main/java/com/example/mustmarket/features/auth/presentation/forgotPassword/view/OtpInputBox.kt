package com.example.mustmarket.features.auth.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpInputBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onBackSpace: () -> Unit,
    focusRequester: FocusRequester,
    boxSize: Dp = 50.dp,
    boxBackgroundColor: Color = Color.White,
    boxBorderColor: Color = Color.Gray,
    textColor: Color = Color.Black,
    isFirstBox: Boolean = false,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        ),
        singleLine = true,
        modifier = modifier
            .size(boxSize)
            .focusRequester(focusRequester)
            .border(
                width = 1.dp,
                color = boxBorderColor,
                shape = MaterialTheme.shapes.small
            )
            .background(
                color = boxBackgroundColor,
                shape = MaterialTheme.shapes.small
            ),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                innerTextField()
            }
        },
        textStyle = TextStyle(
            color = textColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    )

    LaunchedEffect(Unit) {
        if (isFirstBox) {
            focusRequester.requestFocus()
        }
    }
}