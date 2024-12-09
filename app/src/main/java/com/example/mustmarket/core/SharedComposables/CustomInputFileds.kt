package com.example.mustmarket.core.SharedComposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed


@Composable
fun PasswordInput(
    onInputChanged: (String) -> Unit,
    inputText: String,
    showPassword: Boolean,
    toggleShowPassword: (Boolean) -> Unit,
    name: String,
    errorMessage: List<String> = emptyList(),
    passwordStrength: PasswordStrength? = null
) {


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorCursorColor = MaterialTheme.colors.primary,
                errorBorderColor = Color.Gray,
                focusedBorderColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier
                .background(Color.Transparent),
            value = inputText,
            onValueChange = { onInputChanged(it) },
            textStyle = MaterialTheme.typography.h4.copy(
                color = ThemeUtils.AppColors.Text.themed()
            ),
            label = { TextFieldLabel(name = name) },
            singleLine = true,

            keyboardOptions = myKeyboardOptions,
            trailingIcon = {
                IconButton(onClick = { toggleShowPassword(!showPassword) }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPassword) "Hide Password" else "Show password"
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = errorMessage.isNotEmpty()
        )
        AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
                errorMessage.forEach { error ->
                    Text(
                        text = "• $error",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
        passwordStrength?.let { strength ->
            Spacer(modifier = Modifier.height(8.dp))
            when (strength) {
                PasswordStrength.WEAK -> {
                    PasswordStrengthIndicator(
                        strength = "Weak",
                        color = Color.Red,
                    )
                }

                PasswordStrength.MEDIUM -> {
                    PasswordStrengthIndicator(
                        strength = "Medium",
                        color = Color.Yellow,
                    )
                }

                PasswordStrength.STRONG -> {
                    PasswordStrengthIndicator(
                        strength = "Strong",
                        color = Color.Green,
                    )
                }

                PasswordStrength.VERY_STRONG -> {
                    PasswordStrengthIndicator(
                        strength = "Very Strong",
                        color = Color.Green,
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
fun ProductInputFields(
    onInputChanged: (String) -> Unit,
    inputText: String,
    labelName: String,
    keyboardType: KeyboardType,
    isDescription: Boolean = false,
) {
    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorCursorColor = MaterialTheme.colors.primary,
            errorBorderColor = Color.Gray,
            focusedBorderColor = MaterialTheme.colors.primary
        ),
        value = inputText,
        onValueChange = { onInputChanged(it) },
        textStyle = MaterialTheme.typography.h4.copy(
            color = ThemeUtils.AppColors.Text.themed()
        ),
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .height(
                if (isDescription) 130.dp else 60.dp
            ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        label = { TextFieldLabel(name = labelName) },
    )
}

@Composable
fun DefaultTextInput(
    onInputChanged: (String) -> Unit,
    inputText: String,
    name: String,
    errorMessage: String? = null,
    onSubmitted: (() -> Unit)? = null
) {
    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorCursorColor = MaterialTheme.colors.primary,
            errorBorderColor = Color.Gray,
            focusedBorderColor = MaterialTheme.colors.primary
        ),
        value = inputText,
        onValueChange = { onInputChanged(it) },
        textStyle = MaterialTheme.typography.h4.copy(
            color = ThemeUtils.AppColors.Text.themed()
        ),
        modifier = Modifier
            .background(Color.Transparent),
        singleLine = true,
        keyboardOptions = myKeyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                onSubmitted?.invoke()
            }
        ),
        label = { TextFieldLabel(name = name) },
        isError = errorMessage != null
    )
    errorMessage?.let {
        Text(
            text = it,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun TextFieldLabel(name: String) {
    Text(
        text = name, style = MaterialTheme.typography.h3,
        color = Color(0xff727272),
        textAlign = TextAlign.Start,
        lineHeight = 29.sp
    )
}

val myKeyboardOptions = KeyboardOptions(
    keyboardType = KeyboardType.Email,
    imeAction = ImeAction.Done
)

@Composable
fun ButtonLoading(
    name: String,
    isLoading: Boolean,
    enabled: Boolean,
    onClicked: () -> Unit,
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        if (!isLoading) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(60.dp),
                enabled = enabled,
                shape = RoundedCornerShape(30),
                onClick = {
                    onClicked()
                }
            ) {
                Text(text = name, style = MaterialTheme.typography.button)
            }
        } else {
            LoadingState()
        }
    }
}