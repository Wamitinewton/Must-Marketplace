package com.newton.mustmarket.features.merchant.create_store.presentation.view.merchant_onboarding

import android.widget.Button
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newton.mustmarket.R
import com.newton.mustmarket.core.sharedComposable.LoopReverseLottieLoader

@Composable
fun MerchantOnboardingScreen(
    onRegisterMerchantClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
) {
    val benefits = listOf(
        Benefit(
            "Reach More Customers",
            "Access our large customer base and expand your business reach"
        ),
        Benefit(
            "Easy Management",
            "Powerful tools to manage your inventory and orders"
        ),
        Benefit(
            "Secure Payments",
            "Reliable payment processing and instant transfers"
        ),
        Benefit(
            "24/7 Support",
            "Dedicated support team to help you succeed"
        )
    )
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LoopReverseLottieLoader(
                lottieFile = R.raw.merchant_onboarding,
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }

        Text(
            text = "Become a merchant",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        MerchantBenefitCard(benefits = benefits)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onRegisterMerchantClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colors.primary
                )
            ) {
                Text(
                    text = "Register as merchant",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 16.sp
                )
            }
            TextButton(
                onClick = onSkipClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Maybe Later",
                    fontSize = 16.sp
                )
            }
        }
    }
}