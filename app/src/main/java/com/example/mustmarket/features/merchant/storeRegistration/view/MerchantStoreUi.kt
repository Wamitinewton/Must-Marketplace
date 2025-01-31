package com.example.mustmarket.features.merchant.storeRegistration.view

import androidx.compose.runtime.Composable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.mustmarket.core.sharedComposable.ButtonLoading
import com.example.mustmarket.core.sharedComposable.DefaultTextInput
import com.example.mustmarket.features.merchant.storeRegistration.mDataStore.Product
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun MerchantStoreScreen(
    navController: NavController,
    storeName: String = "",
    storeDescription: String = "",
    storeLogoUri: String = "",
    products: List<Product> = emptyList(),
    onAddProduct: (() -> Unit)? = null,
    onEditProduct: ((Product) -> Unit)? = null,
    onDeleteProduct: ((Product) -> Unit)? = null,
    onUpdateStoreDetails: ((String, String, Uri?) -> Unit)? = null
) {
    var updatedStoreDescription by remember {
        mutableStateOf(storeDescription)
    }

    var updatedStoreName by remember {
        mutableStateOf(storeName)
    }

    var storeImageUri by remember {
        mutableStateOf<Uri?>(Uri.parse(storeLogoUri))
    }

    val imagePickerLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ){ uri: Uri? ->
        storeImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Stall",
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
                }
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick =  { onAddProduct?.invoke()},
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product",
                    tint = ThemeUtils.AppColors.Surface.themed()
                )
            }

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            storeImageUri?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "Store Logo",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Button(onClick = { imagePickerLaunch.launch("image/*") }) {
                Text("Change Store Logo")
            }

            DefaultTextInput(
                onInputChanged = { updatedStoreName = it },
                inputText = updatedStoreName,
                name = "Store Name"
            )
            DefaultTextInput(
                onInputChanged = { updatedStoreDescription = it },
                inputText = updatedStoreDescription,
                name = "Store Description"
            )

            ButtonLoading(
                name = "Update Store Details",
                isLoading = false,
                enabled = true,
                onClicked = {
                    onUpdateStoreDetails?.invoke(updatedStoreName, updatedStoreDescription, storeImageUri)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Your Products", fontSize = 18.sp, color = ThemeUtils.AppColors.Text.themed())

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberImagePainter(product.imageUri),
                                contentDescription = "Product Image",
                                modifier = Modifier.size(60.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Price: ${product.price}", fontSize = 14.sp, color = Color.Gray)
                            }

                            IconButton(onClick = { onEditProduct?.invoke(product) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Product")
                            }

                            IconButton(onClick = { onDeleteProduct?.invoke(product) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Product", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}