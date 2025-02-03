package com.example.mustmarket.features.home.presentation.view.productList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.R
import com.example.mustmarket.core.sharedComposable.ErrorState
import com.example.mustmarket.core.sharedComposable.shimmer.ProductShimmer
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.presentation.event.CategoryEvent
import com.example.mustmarket.features.home.presentation.event.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.viewmodels.AllProductsViewModel
import com.example.mustmarket.features.home.presentation.viewmodels.ProductCategoryViewModel
import com.example.mustmarket.features.home.presentation.viewmodels.SharedViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.colorPrimary
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber

@Composable
fun HomeScreen(
    navController: NavController,
    allProductsViewModel: AllProductsViewModel,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {

    Content(
        viewModel = allProductsViewModel,
        navController = navController,
        sharedViewModel = sharedViewModel
    )
}

@Composable
fun Content(
    viewModel: AllProductsViewModel = hiltViewModel(),
    categoryViewModel: ProductCategoryViewModel = hiltViewModel(),
    navController: NavController,

    sharedViewModel: SharedViewModel,

    loginViewModel: LoginViewModel = hiltViewModel()

) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.productsUiState.collectAsState()
    val categoryUIState by categoryViewModel.uiState.collectAsState()
    val isRefreshing = categoryUIState.isRefreshing || uiState.isRefreshing
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = isRefreshing
    )
    val user by loginViewModel.loggedInUser.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = ThemeUtils.AppColors.Surface.themed(),
                title = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(40.dp)
                            .clickable(
                                onClick = {
                                    navController.navigate(Screen.ProductSearch.route)
                                }
                            ),
                        backgroundColor = ThemeUtils.AppColors.Text.themed()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 35.dp)
                        ) {
                            Text(
                                text = "Search products....",
                                style = MaterialTheme.typography.caption
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                tint = Color.Black,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                coroutineScope.launch {
                    supervisorScope {
                        val productRefresh = async {
                            viewModel.onProductEvent(HomeScreenEvent.Refresh)
                        }
                        val categoryRefresh = async {
                            categoryViewModel.onCategoryEvent(CategoryEvent.Refresh)
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
                    .padding(padding)
                    .padding(bottom = 60.dp, top = 20.dp)
            ) {

                item {
                    HeaderBar(userName = user?.name)
                }

                item { Promotions() }
                item { CategoryGridView() }
                item {
                    Card(
                        shape = RectangleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                        ) {
                            Text(
                                text = "All Products",
                                color = ThemeUtils.AppColors.Text.themed(),
                                fontSize = 18.sp
                            )

                           if (uiState.products.isNotEmpty() && uiState.errorMessage.isNullOrEmpty()) {
                               Text(
                                   modifier = Modifier
                                       .clickable(
                                           onClick = {
                                               val products = uiState.products
                                               sharedViewModel.addProductList(products)
                                               navController.navigate(Screen.AllProductsList.route) {
                                                   popUpTo(Screen.AllProductsList.route) {
                                                       inclusive = true
                                                   }
                                               }
                                           }
                                       ),
                                   text = "See all",
                                   color = ThemeUtils.AppColors.Text.themed(),
                                   fontSize = 16.sp
                               )
                           }
                        }
                    }
                }



                when {
                    uiState.isLoading -> {
                        items(count = 5){
                            ProductShimmer()
                        }
                    }

                    uiState.errorMessage?.isNotEmpty() == true -> {
                        item {
                            uiState.errorMessage?.let { ErrorState(message = it) }
                            uiState.errorMessage?.let { Timber.tag("Error").d(it) }
                        }
                    }

                    uiState.products.isEmpty() -> {
                        item {
                            Text(
                                text = "No products"
                            )
                        }
                    }


                    else -> {
                        items(uiState.products.size) { index ->
                            val product = uiState.products[index]
                            ProductCard(
                                product = product,
                                onClick = {
                                    val productDetails = NetworkProduct(
                                        name = product.name,
                                        id = product.id,
                                        brand = product.brand,
                                        price = product.price,
                                        images = product.images,
                                        category = product.category,
                                        userData = product.userData,
                                        inventory = product.inventory,
                                        description = product.description
                                    )
                                    sharedViewModel.addDetails(productDetails)
                                    navController.navigate(Screen.Detail.route) {
                                        popUpTo(Screen.Detail.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                            if (index < uiState.products.size - 1) {
                                Divider(
                                    color = Color.Gray,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderBar(
    userName: String?
) {
    Card(
        Modifier
            .height(64.dp)
            .padding(horizontal = 16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = colorPrimary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName?.firstOrNull()?.uppercase() ?: "Unknown",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = userName ?: "Unknown",
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colors.primary
                )
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