package com.newton.mustmarket.features.merchant.create_store.presentation.view.merchant_onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newton.mustmarket.core.sharedComposable.AnimatedText

data class Benefit(
    val title: String,
    val description: String
)

@Composable
fun MerchantBenefitCard(benefits: List<Benefit>, onBecomeMerchant: () -> Unit = {}) {
    var currentBenefitIndex by remember { mutableIntStateOf(0) }
    var progress = remember { androidx.compose.animation.core.Animatable(0f) }
    var showRegisterMerchantPrompt by remember { mutableStateOf(false) }

    val timerDuration = 5000

    LaunchedEffect(currentBenefitIndex) {
        if (currentBenefitIndex < benefits.size - 1) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = timerDuration, easing = LinearEasing)
            )
            currentBenefitIndex = (currentBenefitIndex + 1) % benefits.size
        } else {
            showRegisterMerchantPrompt = true
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = if (showRegisterMerchantPrompt) -1 else currentBenefitIndex,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                }
            ) { index ->
              if (index == -1) {
                 Column(
                     modifier = Modifier
                         .fillMaxSize()
                         .padding(16.dp),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     AnimatedText(
                         text = "Ready to become a merchant?",
                         fontSize = 24,
                         fontWeight = FontWeight.Bold
                     )
                     Spacer(modifier = Modifier.height(16.dp))
                     Button(
                         onClick = onBecomeMerchant,
                         modifier = Modifier.padding(top = 8.dp)
                     ) {
                         Text("Get Started")
                     }
                 }
              } else {
                  Column(
                      modifier = Modifier
                          .fillMaxSize()
                          .padding(16.dp),
                      verticalArrangement = Arrangement.Center,
                      horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                      Text(
                          text = benefits[index].title,
                          fontSize = 24.sp,
                          fontWeight = FontWeight.Bold,
                          textAlign = TextAlign.Center
                      )
                      Spacer(modifier = Modifier.height(8.dp))
                      Text(
                          text = benefits[index].description,
                          fontSize = 16.sp,
                          textAlign = TextAlign.Center,
                          color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                      )
                  }
              }
            }

          if (!showRegisterMerchantPrompt) {
              LinearProgressIndicator(
                  progress = progress.value,
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(4.dp)
                      .align(Alignment.BottomCenter),
                  color = MaterialTheme.colors.primary,

                  )
          }
        }
    }

}