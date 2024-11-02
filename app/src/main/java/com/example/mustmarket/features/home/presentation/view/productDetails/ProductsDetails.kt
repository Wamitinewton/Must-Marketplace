package com.example.mustmarket.features.home.presentation.view.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.ErrorState
import com.example.mustmarket.core.SharedComposables.LoadingState
import com.example.mustmarket.core.util.Constants.formatPrice
import com.example.mustmarket.core.util.SingleToastManager
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.presentation.viewmodels.AllProductsViewModel
import com.example.mustmarket.features.home.presentation.viewmodels.BookmarksViewModel
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.coroutines.launch

@Composable
fun ProductDetailsScreen(
    productId: Int,
    onBackPressed: () -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel(),
    productsViewModel: AllProductsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by productsViewModel.productsUiState.collectAsState()
    uiState.products
    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.successEvent.collect { message ->
                message?.let {
                    SingleToastManager.showToast(
                        context = context,
                        message = it,
                        scope = scope
                    )
                }
            }
        }
        launch {
            viewModel.errorEvent.collect { message ->
                message?.let {
                    SingleToastManager.showToast(
                        context = context,
                        message = it,
                        scope = scope
                    )
                }
            }
        }
    }

    LaunchedEffect(productId) {
        productsViewModel.loadProductDetails(productId = productId)
    }

    when {
        uiState.isLoading -> LoadingState()
        uiState.errorMessage.isNotBlank() -> ErrorState()
        uiState.products.isNotEmpty() -> {
            ProductDetailsContent(
                product = uiState.products[productId],
                onBackPressed = onBackPressed,
                bookmarksViewModel = viewModel
            )
        }
    }
}


@Composable
fun ProductDetailsContent(
    product: NetworkProduct,
    onBackPressed: () -> Unit,
    bookmarksViewModel: BookmarksViewModel
) {
    val bookmarkStatuses = bookmarksViewModel.bookmarkStatusUpdates.collectAsState()
    val isBookmarked = bookmarkStatuses.value[product.id] ?: false

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        product.name,
                        style = MaterialTheme.typography.h6
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { bookmarksViewModel.toggleBookmarkStatus(product) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) MaterialTheme.colors.primary else Color.Gray
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colors.surface,
                                MaterialTheme.colors.surface.copy(alpha = 0.5f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    GlideImage(
                        imageModel = { product.imageUrl },
                        modifier = Modifier.fillMaxSize(),
                        component = rememberImageComponent {
                            +ShimmerPlugin(
                                Shimmer.Flash(
                                    baseColor = Color.White,
                                    highlightColor = Color.LightGray
                                )
                            )
                        },
                        failure = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.no_image),
                                    contentDescription = "No Image",
                                    modifier = Modifier.size(100.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatPrice(product.price),
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.secondaryVariant
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "4.5",
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.body1.copy(
                        color = ThemeUtils.AppColors.SecondaryText.themed()
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

//                Button(
//                    onClick = {
//
//                    },
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = MaterialTheme.colors.primary
//                    )
//                ) {
//
//                }
            }
        }
    }
}