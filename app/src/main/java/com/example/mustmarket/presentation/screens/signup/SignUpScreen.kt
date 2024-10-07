package com.example.mustmarket.presentation.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mustmarket.R
import com.example.mustmarket.presentation.components.SearchTextField


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color(0xfffcfcfc)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(
                id = R.drawable.ic_min_carrot
            ),
            contentDescription = null
            )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Sign up",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Enter your credentials to continue",
            style = MaterialTheme.typography.h3,
            color = Color(0xff727272),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )

    }
}