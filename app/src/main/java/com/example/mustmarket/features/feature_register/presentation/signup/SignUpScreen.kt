package com.example.mustmarket.features.feature_register.presentation.signup

import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.SharedComposables.ButtonLoading
import com.example.mustmarket.SharedComposables.MyTextField
import com.example.mustmarket.SharedComposables.PasswordInput


@Composable
fun SignUpScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                name = "Password"
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
            Button(
                onClick = {
                    Toast.makeText(context, "Feature not added", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color(0xff5383ec)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign in with Google",
                    style = MaterialTheme.typography.button,
                )
            }

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
                        navController.popBackStack()
                        navController.navigate(Screen.Login.route)
                    }
                ) {
                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.h6,
                        fontFamily = FontFamily(
                            Font(
                                R.font.gilroysemibold,
                                weight = FontWeight.SemiBold
                            )
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

        }
    }
}