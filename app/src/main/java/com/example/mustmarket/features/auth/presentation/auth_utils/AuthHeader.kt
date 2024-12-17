package com.example.mustmarket.features.auth.presentation.auth_utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mustmarket.R
import com.example.mustmarket.core.sharedComposable.LoopReverseLottieLoader


@Composable
fun AuthHeader(
    modifier: Modifier = Modifier,
    authTitle: String,
    authText: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoopReverseLottieLoader(
            lottieFile = R.raw.welcome,
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = authTitle,
            style = MaterialTheme.typography.h2.copy(
                color = MaterialTheme.colors.primary
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Text(
            text = authText,
            style = MaterialTheme.typography.h3,
            color = Color(0xff727272),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }
}