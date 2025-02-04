package com.newton.mustmarket.features.auth.presentation.signup.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TermsAndServices() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "By continuing you agree to our",
            style = MaterialTheme.typography.h5
        )
        Text(
            text = "Terms of service",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )
        Text(text = "and ", style = MaterialTheme.typography.h5)
        Text(
            text = "Privacy Policy.",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )
    }
}