package com.example.mustmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mustmarket.presentation.screens.signup.SignUpScreen
import com.example.mustmarket.ui.theme.MustMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MustMarketTheme {
                SignUpScreen()
            }
        }
    }
}

