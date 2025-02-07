package com.newton.mustmarket.features.get_products.presentation.view.productList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.newton.mustmarket.core.sharedComposable.ErrorComposable
import com.newton.mustmarket.core.sharedComposable.shimmer.ProductShimmer
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.get_products.presentation.event.HomeScreenEvent
import com.newton.mustmarket.features.get_products.presentation.viewmodels.AllProductsViewModel
import com.newton.mustmarket.features.get_products.presentation.viewmodels.SharedViewModel
import com.newton.mustmarket.navigation.Screen
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun ProductsByCategoryScreen(
    categoryName: String,
    navController: NavController,
    viewModel: AllProductsViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    viewModel.onProductEvent(HomeScreenEvent.OnCategoryClicked(categoryName))

    val uiState by viewModel.getProductsByCategoryState.collectAsState()



    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = ThemeUtils.AppColors.Surface.themed(),
                title = {
                    Text(
                        text = categoryName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2
                    )
                },
                navigationIcon = {
                    IconButton(onClick ={ navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(5) {
                            ProductShimmer()
                        }
                    }
                }
                uiState.errorMessage != null -> {
                    ErrorComposable(
                        message = uiState.errorMessage ?: "Error occurred",
                        onRetry = {
                            viewModel.onProductEvent(HomeScreenEvent.OnCategoryClicked(categoryName))
                        }
                    )
                }

                uiState.products.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No products found for this category",
                            style = MaterialTheme.typography.h6
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
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
                                        popUpTo(Screen.Detail.route){
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}