package com.example.mustmarket.features.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.example.mustmarket.features.home.data.mapper.toNetworkProduct
import com.example.mustmarket.features.home.presentation.view.ProductCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDelete(
    product: BookmarkedProduct,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    var isRevealedState by remember { mutableStateOf(false) }
    val sizes = androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isRevealedState) -120f else 0f,
        label = ""
    )
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            when (dismissValue) {
                DismissValue.DismissedToEnd -> {
                    isRevealedState = true
                    true
                }

                DismissValue.DismissedToStart -> false
                DismissValue.Default -> {
                    isRevealedState = false
                    false
                }
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            DeleteBackground(swipeProgress = dismissState.progress.fraction)
        },
        dismissContent = {
            ProductCard(
                product = product.toNetworkProduct(),
                onClick = onClick
            )
        },
        directions = setOf(DismissDirection.StartToEnd)
    )
}

@Composable
fun DeleteBackground(swipeProgress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Red.copy(alpha = 0.8f * swipeProgress))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Delete",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}