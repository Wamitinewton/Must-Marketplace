package com.example.mustmarket.presentation.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.presentation.components.ButtonLoading
import com.example.mustmarket.presentation.components.MyTextField
import com.example.mustmarket.presentation.components.PasswordInput


@Composable
fun SignUpScreen(
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
        MyTextField(
            onInputChanged = {},
            inputText = "",
            name = "Name"
        )
        MyTextField(
            onInputChanged = {},
            inputText = "",
            name = "Email"
        )
        PasswordInput(
            onInputChanged = {},
            inputText = "",
            showPassword = false,
            toggleShowPassword = {},
            name = "Confirm Password"
        )

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

        Spacer(modifier = Modifier.height(22.dp))
        ButtonLoading(
            name = "Sign Up",
            isLoading = false,
            enabled = true,
            onClicked = {}
        )
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.h6,
                fontFamily = FontFamily(
                    Font(R.font.gilroysemibold, weight = FontWeight.SemiBold)
                ),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 8.dp)
            )
            IconButton(
                onClick = {
                }
            ) {
                Text(
                    text = "Sign in",
                    style = MaterialTheme.typography.h6,
                    fontFamily = FontFamily(Font(R.font.gilroysemibold,
                        weight = FontWeight.SemiBold
                        )),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary
                )
            }
        }

    }
}