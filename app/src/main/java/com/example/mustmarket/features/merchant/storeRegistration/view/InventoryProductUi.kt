package com.example.mustmarket.features.merchant.storeRegistration.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mustmarket.features.merchant.storeRegistration.mDataStore.Product
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun MerchantProductScreen(
    navController: NavController,
    products: List<Product> = emptyList(),
    onEditProduct: ((Product) -> Unit)? = null,
    onDeleteProduct: ((Product) -> Unit)? = null,
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Inventory Products",
                        fontSize = 18.sp,
                        color = ThemeUtils.AppColors.Text.themed()
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
                                .size(24.dp)
                        )
                    }
                }
            )
        },

    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(MaterialTheme.colors.background)
        ){
            Text(
                "My Stall Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ThemeUtils.AppColors.Text.themed()
            )

        }
    }
}