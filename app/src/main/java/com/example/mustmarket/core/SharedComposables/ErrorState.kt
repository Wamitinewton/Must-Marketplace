package com.example.mustmarket.core.SharedComposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mustmarket.R

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onRetry: () -> Unit = {}
) {
    AnimatedErrorEntrance(
        visible = visible,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .border(
                    width = 1.dp, color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center,


            ) {
            Text(
                text = "Server error. Try again later",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun AnimatedErrorEntrance(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = visible
        }
    }
    AnimatedVisibility(
        visibleState = transitionState,
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 500)
        ) + slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { fullHeight -> -fullHeight / 3 }
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 500)
        ) + slideOutVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessVeryLow
            ),
            targetOffsetY = { fullHeight -> -fullHeight / 3 }
        )
    ) {
        content()
    }
}


@Composable
fun NoSearchResultsState(searchQuery: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search), // Make sure you have this icon in your resources
                contentDescription = "No results found",
                modifier = Modifier.size(48.dp),
                tint = Color.Gray
            )
            Text(
                text = "No products found",
                style = MaterialTheme.typography.h6,
                color = Color.Gray
            )
            Text(
                text = "We couldn't find any products matching '$searchQuery'",
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Try checking your spelling or using different keywords",
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}