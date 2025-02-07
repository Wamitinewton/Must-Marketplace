package com.newton.mustmarket.features.get_products.presentation.view.productList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.newton.mustmarket.R
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import com.newton.mustmarket.features.get_products.presentation.viewmodels.SharedViewModel
import com.newton.mustmarket.navigation.Screen
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun AllProductsListScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {

    val products = sharedViewModel.productList

    Scaffold(
        topBar = {
            TopAppBar(
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
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            when(products) {
                is List<NetworkProduct> -> {
                    items(products) { product ->
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
                                navController.navigate(Screen.Detail.route){
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

