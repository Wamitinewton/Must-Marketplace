package com.example.mustmarket.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.presentation.components.ButtonLoading
import com.example.mustmarket.presentation.components.MyTextField
import com.example.mustmarket.presentation.components.PasswordInput

@Composable
fun LoginScreen(
    navController: NavController
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xfffcfcfc)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 62.dp),
                painter = painterResource(id = R.drawable.ic_min_carrot),
                contentDescription = stringResource(id = R.string.logo)
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 6.dp),
                text = "Enter your email and password",
                style = MaterialTheme.typography.h3,
                color = Color(0xff727272),
                textAlign = TextAlign.Start
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
                name = "Password"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                text = "Forgot Password",
                style = MaterialTheme.typography.h6
            )
            ButtonLoading(
                name = "Login",
                isLoading = false,
                enabled = true,
                onClicked = {}
            )
            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}