package com.example.mustmarket.features.merchant.store.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.features.merchant.store.mDataStore.Product
import com.example.mustmarket.features.merchant.store.presentation.EditStoreDropdownMenu
import com.example.mustmarket.features.merchant.store.viewModel.MerchantViewModel
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
    products: List<Product> = emptyList(),
    //onAddProduct: (() -> Unit)? = null,
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

    var isUploading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val imagePickerLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ){ uri: Uri? ->
        storeImageUri = uri
    }

    val stallLogo by merchantViewModel.storeProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                            .padding(2.dp)
                            .size(30.dp)
                            .clip(CircleShape)
                    ) {

                        storeImageUri?.let {
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = "Store Logo",
                                modifier = Modifier
                                    .clickable {
                                        imagePickerLaunch.launch("image/*")
                                    }
                                    .size(100.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

//                        stallLogo?.let { profile ->
//                            Column {
//                                profile.storeLogoUrl?.let { storeLogoUrl ->
//                                    AsyncImage(
//                                        model = storeLogoUrl,
//                                        contentDescription = "Stall Logo",
//                                        modifier = Modifier
//                                            .size(40.dp)
//                                            .clip(CircleShape)
//                                            .background(
//                                                color = Color.White
//                                            )
//                                            .clickable {
//                                                imagePickerLaunch.launch("image/*")
//                                            },
//                                        contentScale = ContentScale.Crop
//                                    )
//                                }
//                            }
//                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

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

                    EditStoreDropdownMenu(
                        storeName = updatedStoreName,
                        storeDescription = updatedStoreDescription,
                        storeImageUri = storeImageUri,
                        onUpdateStoreDetails = { name, description, imageUri ->
                            onUpdateStoreDetails?.invoke(name, description, imageUri)
                        }
                    )
                }
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick =  {
                    navController.navigate(Screen.AddProduct.route)
                },
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
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(
                "My Stall Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ThemeUtils.AppColors.Text.themed()
            )

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
                                painter = rememberAsyncImagePainter(product.imageUri),
                                contentDescription = "Product Image",
                                modifier = Modifier.size(60.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    product.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ThemeUtils.AppColors.Text.themed()
                                )

                                Text(
                                    "Price: ${product.price}",
                                    fontSize = 14.sp,
                                    color = ThemeUtils.AppColors.Text.themed()
                                )
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

