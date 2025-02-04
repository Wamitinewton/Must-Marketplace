package com.example.mustmarket.features.merchant.storeRegistration.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.features.merchant.storeRegistration.mDataStore.Product
import com.example.mustmarket.features.merchant.storeRegistration.presentation.InventoryCard
import com.example.mustmarket.features.merchant.storeRegistration.viewModel.MerchantViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun MerchantStoreScreen(
    merchantViewModel: MerchantViewModel = viewModel(),
    navController: NavController,
    merchantId: String = "",
    storeName: String = "",
    storeDescription: String = "",
    storeLogoUri: String = "",
) {
    var updatedStoreDescription by remember {
        mutableStateOf(storeDescription)
    }

    var updatedStoreName by remember {
        mutableStateOf(storeName)
    }

    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Text(
                        text = updatedStoreName.ifEmpty { "unknown merchant" },
                        fontWeight = FontWeight.Bold,
                        color = ThemeUtils.AppColors.Text.themed(),
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            Icons.Default.KeyboardDoubleArrowLeft,
                            contentDescription = "Back",
                            tint = ThemeUtils.AppColors.Surface.themed(),
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                },
                actions = {

                    Box (
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                            navController.navigate(Screen.StoreProfileScreen.route)
                        }
                            .padding(5.dp),
                    ){
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "profile",
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }

                }
            )

        },

    ) { padding ->
        Row (
            Modifier
                .background(
                    MaterialTheme.colors.surface
                )
                .fillMaxWidth()
        ){
            Text(
                updatedStoreDescription,
                fontSize = 15.sp,
                color = ThemeUtils.AppColors.Text.themed(),
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            InventoryCard(
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.product),
                        contentDescription = "Products",
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primarySurface),
                        modifier = Modifier
                            .size(50.dp)
                    )
                },
                text = "products".uppercase(),
                onClick = {
                    navController.navigate(Screen.InventoryProducts.route)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            InventoryCard(
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.add_item),
                        contentDescription = "Add Stock",
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primarySurface),
                        modifier = Modifier
                            .size(50.dp)

                    )
                },
                text = "ADD STOCK",
                onClick = {
                    navController.navigate(Screen.AddProduct.route)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            InventoryCard(
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.remove_stock),
                        contentDescription = "Remove Stock",
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primarySurface),
                        modifier = Modifier
                            .size(50.dp)
                    )
                },
                text = "REMOVE STOCK",
                onClick = {
                    Toast.makeText(
                        context, "Coming Soon", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}

