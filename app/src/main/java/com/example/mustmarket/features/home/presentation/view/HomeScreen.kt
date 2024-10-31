package com.example.mustmarket.features.home.presentation.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.AppBarPrimary
import com.example.mustmarket.core.SharedComposables.ErrorState
import com.example.mustmarket.core.SharedComposables.LoadingState
import com.example.mustmarket.features.home.domain.model.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.viewmodels.AllProductsViewModel
import com.example.mustmarket.features.home.presentation.viewmodels.BookmarksViewModel
import com.example.mustmarket.features.home.presentation.viewmodels.ProductCategoryViewModel
import com.example.mustmarket.ui.theme.colorPrimary
import com.example.mustmarket.ui.theme.favourite
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@Composable
fun HomeScreen(
    navController: NavController,
    allProductsViewModel: AllProductsViewModel = hiltViewModel(),
) {

    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .offset(0.dp, (-30).dp),
            painter = painterResource(id = R.drawable.bg_main),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Content(
            viewModel = allProductsViewModel,
            onProductClick = {},

            )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Content(
    viewModel: AllProductsViewModel = hiltViewModel(),
    categoryViewModel: ProductCategoryViewModel = hiltViewModel(),
    onProductClick: (NetworkProduct) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.productsUiState.collectAsState()
    val categoryUIState by categoryViewModel.uiState.collectAsState()
    val isRefreshing = categoryUIState.isRefreshing || uiState.isRefreshing
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = isRefreshing
    )

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            coroutineScope.launch {
                supervisorScope {
                    val productRefresh = async {
                        viewModel.onProductEvent(HomeScreenEvent.Refresh)
                    }
                    val categoryRefresh = async {
                        categoryViewModel.onCategoryEvent(HomeScreenEvent.Refresh)
                    }
                    productRefresh.await()
                    categoryRefresh.await()
                }
            }
        }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            item { AppBarPrimary() }
            stickyHeader {
                HeaderBar()
            }
            item { Promotions() }
            item { CategoryGridView() }
//            item { TopProduct() }
            item {
                Card(
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(
                            text = "All Products",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            when {
                uiState.isLoading -> {
                    item {
                        LoadingState()
                    }
                }

                uiState.errorMessage.isNotEmpty() -> {
                    item {
                     ErrorState()
                    }
                }

                else -> {
                    items(uiState.products.size) { index ->
                        val product = uiState.products[index]
                        ProductCard(

                            product = product,
                            onClick = { onProductClick(product) },
                        )
                        if (index < uiState.products.size - 1) {
                            Divider(
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun HeaderBar() {
    Card(
        Modifier
            .height(64.dp)
            .padding(horizontal = 16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QrButton()
            VerticalDivider()
            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {}
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    tint = Color(0xFF6FCF97)
                )
                Column(Modifier.padding(8.dp)) {
                    Text(
                        text = "Account",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Newton Wamiti",
                        fontWeight = FontWeight.SemiBold,
                        color = colorPrimary,
                        fontSize = 12.sp
                    )
                }
            }
            VerticalDivider()
            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {}
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = null,
                    tint = favourite
                )
                Column(
                    Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "My wishlist",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "WishList",
                        fontWeight = FontWeight.SemiBold,
                        color = favourite,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QrButton() {
    IconButton(
        onClick = {},
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_scan),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Composable
fun VerticalDivider() {
    Divider(
        color = Color(0xFFF1F1F1),
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
    )
}

@Composable
fun Promotions() {
    Column {
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = "Promos & discounts \uD83D\uDE0A",

                style = MaterialTheme.typography.body1.copy(

                    fontSize = 20.sp
                ),
                maxLines = 1
            )
        }
        LazyRow(
            Modifier
                .height(160.dp)
                .padding(top = 20.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PromotionItem(
                    imagePainter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg")
                )
            }
            item {
                PromotionItem(
                    imagePainter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_4.jpg")
                )
            }
        }
    }
}

@Composable
fun PromotionItem(
    backgroundColor: Color = Color.Transparent,
    imagePainter: Painter
) {
    Card(
        Modifier.width(300.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = backgroundColor,
        elevation = 0.dp
    ) {
        Row {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                alignment = Alignment.CenterEnd,
                contentScale = ContentScale.Crop
            )
        }
    }
}









