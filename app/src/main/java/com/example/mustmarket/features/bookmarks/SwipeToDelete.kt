package com.example.mustmarket.features.bookmarks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.ImageLoaderUtil
import com.example.mustmarket.core.util.Constants.formatPrice
import com.example.mustmarket.features.home.data.local.entities.BookmarkedProduct
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDelete(
    product: BookmarkedProduct,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isRevealedState by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val sizes = androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isRevealedState) -120f else 0f,
        label = ""
    )
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            when (dismissValue) {
                DismissValue.DismissedToEnd -> {
                    showConfirmDialog = true
                    isRevealedState = true
                    false
                }

                DismissValue.DismissedToStart -> false
                DismissValue.Default -> {
                    isRevealedState = false
                    false
                }
            }
        }
    )

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = {
                showConfirmDialog = false
                scope.launch { dismissState.reset() }
            },
            title = {
                Text(
                    "Confirm Deletion",
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 22.sp,

                    )
                )
            },
            text = {
                Text(
                    "Are you sure you want to remove ${product.name} from bookmarks?",
                    style = MaterialTheme.typography.body1
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDialog = false
                    }
                ) {
                    Text(
                        "Delete",
                        color = MaterialTheme.colors.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        scope.launch { dismissState.reset() }
                    }
                ) {
                    Text(
                        "Cancel"
                    )
                }
            }
        )
    }


    SwipeToDismiss(
        state = dismissState,
        background = {
            DeleteBackground(swipeProgress = dismissState.progress.fraction)
        },
        dismissContent = {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(onClick = onClick),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .height(120.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(100.dp)
                    ) {
                        ImageLoaderUtil(
                            imageUrl = product.imageUrl,
                            contentDescription = "bookmark-image"
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.h6.copy(
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray,
                            maxLines = 2
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatPrice(product.price),
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            )


                        }
                    }
                }
            }
        },
        directions = setOf(DismissDirection.StartToEnd)
    )
}

@Composable
fun DeleteBackground(
    swipeProgress: Float
) {
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