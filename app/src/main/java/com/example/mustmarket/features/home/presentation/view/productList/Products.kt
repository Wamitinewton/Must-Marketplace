package com.example.mustmarket.features.home.presentation.view.productList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mustmarket.core.SharedComposables.CustomImageLoader
import com.example.mustmarket.core.util.Constants.formatPrice
import com.example.mustmarket.core.util.SingleToastManager
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.BookmarkEvent
import com.example.mustmarket.features.home.presentation.viewmodels.BookmarksViewModel
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed


@Composable
fun ProductCard(
    product: NetworkProduct,
    onClick: (Int) -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bookmarkStatuses = viewModel.bookmarkStatusUpdates.collectAsState()

    val isBookmarked = bookmarkStatuses.value[product.id] ?: false

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BookmarkEvent.Success -> {
                    SingleToastManager.showToast(
                        context = context,
                        message = event.message,
                        scope = scope
                    )
                }

                is BookmarkEvent.Error -> {
                    event.message?.let { errorMessage ->
                        SingleToastManager.showToast(
                            context = context,
                            message = errorMessage,
                            scope = scope
                        )
                    }
                }
            }
        }
    }
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding( vertical = 4.dp)
            .clickable(onClick = { onClick(product.id) }),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CustomImageLoader(
                    product.images[0],
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                    )
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.body2,
                    color = ThemeUtils.AppColors.SecondaryText.themed(),
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

                    IconButton(
                        onClick = { viewModel.toggleBookmarkStatus(product) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "bookmarks",
                            tint = if (isBookmarked) MaterialTheme.colors.primary else Color.Gray
                        )
                    }

                }
            }
        }
    }
}

