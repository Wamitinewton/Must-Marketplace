package com.example.mustmarket.features.auth.presentation.auth_utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mustmarket.R

@Composable
fun SignUpPrompt(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    authCheck: String,
    authMethod: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = authCheck,
            style = MaterialTheme.typography.h5,
            fontFamily = FontFamily(
                Font(R.font.gilroysemibold, weight = FontWeight.SemiBold)
            ),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(end = 3.dp)
        )

        TextButton(onClick = onSignUpClick) {
            Text(
                text = authMethod,
                style = MaterialTheme.typography.h6,
                fontFamily = FontFamily(
                    Font(R.font.gilroysemibold, weight = FontWeight.SemiBold)
                ),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.primary,
            )
        }
    }
}