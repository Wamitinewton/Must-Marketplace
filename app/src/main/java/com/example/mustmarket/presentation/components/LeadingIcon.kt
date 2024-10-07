package com.example.mustmarket.presentation.components

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun fieldColors() = TextFieldDefaults.textFieldColors(
    backgroundColor = Color(0xfffcfcfc),
    cursorColor = Color(0xff7c7c7c),
    focusedIndicatorColor = Color(0xffe2e2e2),
    unfocusedIndicatorColor = Color(0xffe2e2e2)
)