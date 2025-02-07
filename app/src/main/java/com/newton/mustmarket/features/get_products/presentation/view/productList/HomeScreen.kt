package com.newton.mustmarket.features.get_products.presentation.view.productList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.newton.mustmarket.R
import com.newton.mustmarket.core.sharedComposable.ErrorState
import com.newton.mustmarket.core.sharedComposable.shimmer.ProductShimmer
import com.newton.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.newton.mustmarket.features.get_products.domain.model.categories.ProductCategory
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.get_products.presentation.event.CategoryEvent
import com.newton.mustmarket.features.get_products.presentation.event.HomeScreenEvent
import com.newton.mustmarket.features.get_products.presentation.viewmodels.AllProductsViewModel
import com.newton.mustmarket.features.get_products.presentation.viewmodels.ProductCategoryViewModel
import com.newton.mustmarket.features.get_products.presentation.viewmodels.SharedViewModel
import com.newton.mustmarket.navigation.Screen
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed
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
                    .padding(bottom = 60.dp)
            ) {


                item { CategoryChipView(
                    categories = categoryUIState.categories,

                    onCategoryClick = { category: ProductCategory ->
                        navController.navigate(Screen.ProductByCategory.createRoute(category = category.name))
                    }
                ) }
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
                                    navController.navigate(Screen.Detail.createRoute(productId = product.id)) {
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


