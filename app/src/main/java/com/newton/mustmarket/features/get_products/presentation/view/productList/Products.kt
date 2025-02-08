package com.newton.mustmarket.features.get_products.presentation.view.productList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.newton.mustmarket.core.sharedComposable.CustomImageLoader
import com.newton.mustmarket.core.util.Constants.formatPrice
import com.newton.mustmarket.core.util.SingleToastManager
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.get_products.presentation.state.BookmarkEvent
import com.newton.mustmarket.features.get_products.presentation.viewmodels.BookmarksViewModel


@Composable
fun ProductCard(
    product: NetworkProduct,
    onClick: ()->  Unit,
    viewModel: BookmarksViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bookmarkStatuses = viewModel.bookmarkStatusUpdates.collectAsState()

    val isBookmarked = bookmarkStatuses.value[product.id] == true

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
            .padding(vertical = 4.dp)
            .clickable(onClick= {onClick()}),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    )
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
                    .padding(top = 5.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Seller",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary.copy(alpha = 0.1f))
                            .padding(4.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = product.userData.name,
                        style = MaterialTheme.typography.caption.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

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

