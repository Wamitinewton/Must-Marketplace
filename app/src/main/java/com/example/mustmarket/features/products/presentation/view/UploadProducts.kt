package com.example.mustmarket.features.products.presentation.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mustmarket.core.networkManager.NetworkConnectionState
import com.example.mustmarket.core.networkManager.rememberConnectivityState
import com.example.mustmarket.features.home.presentation.event.CategoryEvent
import com.example.mustmarket.features.home.presentation.viewmodels.ProductCategoryViewModel
import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.products.presentation.event.UploadEvent
import com.example.mustmarket.features.products.presentation.viewModel.UploadProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UploadProducts(
    productViewModel: UploadProductViewModel = hiltViewModel(),
    categoryViewModel: ProductCategoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") }
    var productBrand by remember { mutableStateOf("") }
    var inventory by remember { mutableStateOf("") }
    val networkState = rememberConnectivityState()
    var showNetworkError by remember { mutableStateOf(false) }
    var wasOffline by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val uiState by productViewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    var isAddingCategory by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryImageUri by remember { mutableStateOf<Uri?>(null) }
    val categoryUiState by categoryViewModel.uiState.collectAsState()

    val categoryLauncherPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> newCategoryImageUri = uri }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.let {
            selectedImages = it
            productViewModel.handleEvent(UploadEvent.MultipleImagesUpload(it, context))
        }
    }


    LaunchedEffect(networkState.value) {
        when (networkState.value) {
            NetworkConnectionState.Unavailable -> {
                showNetworkError = true
                wasOffline = true
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "No internet connection",
                    duration = SnackbarDuration.Short
                )
            }

            NetworkConnectionState.Available -> {
                if (wasOffline) {
                    showNetworkError = true
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Back Online!",
                        duration = SnackbarDuration.Short
                    )
                    delay(3000)
                    showNetworkError = false
                }
            }
        }
    }

    LaunchedEffect(uiState) {
        uiState.error?.let { error ->
            Log.d("Errrrrrooooor", error)
            scaffoldState.snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            productViewModel.handleEvent(UploadEvent.ClearError)
        }
        uiState.uploadData?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Product added successfully",
                duration = SnackbarDuration.Short
            )
            productName = ""
            productDescription = ""
            productPrice = ""
            productCategory = ""
            productBrand = ""
            inventory = ""
            selectedImages = emptyList()
        }
    }

    if (isAddingCategory) {
        Dialog(
            onDismissRequest = { isAddingCategory = false }
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Add New Category",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = {
                            Text(
                                text = "Category Name"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Button(
                        onClick = { categoryLauncherPicker.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Choose category image"
                        )
                    }
                    newCategoryImageUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                categoryViewModel.onCategoryEvent(
                                    CategoryEvent.CategoryUploadEvent(
                                        newCategoryImageUri!!,
                                        newCategoryName,
                                        context
                                    )
                                )
                                isAddingCategory = false
                                newCategoryName = ""
                                newCategoryImageUri = null
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Add"
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                isAddingCategory = false
                                newCategoryName = ""
                                newCategoryImageUri = null
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { hostState ->
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier.padding(top = if (showNetworkError) 48.dp else 50.dp)
            ) { snackbarData ->
                Snackbar(
                    modifier = Modifier.padding(bottom = 50.dp),
                    action = {
                        snackbarData.actionLabel?.let { actionLabel ->
                            TextButton(onClick = { snackbarData.performAction() }) {
                                Text(
                                    text = actionLabel,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }
                    }
                ) {
                    Log.d("Errrrrrooooor", snackbarData.message)
                    Text(snackbarData.message)
                }
            }
        },
        topBar = {
            Column {
                AnimatedVisibility(
                    visible = showNetworkError,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Surface(
                        color = when (networkState.value) {
                            NetworkConnectionState.Available -> Color(0xFF4CAF50)
                            NetworkConnectionState.Unavailable -> Color(0xFFE53935)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (networkState.value) {
                                    NetworkConnectionState.Available -> Icons.Default.CloudDone
                                    NetworkConnectionState.Unavailable -> Icons.Default.CloudOff
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (networkState.value) {
                                    NetworkConnectionState.Available -> "Back Online!"
                                    NetworkConnectionState.Unavailable -> "No connection"
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                TopAppBar(
                    title = {
                        Text(
                            "Add New Product",
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colors.surface)
                        .clickable {
                            launcher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImages.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Images",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Add Product Images",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            items(selectedImages) { imageUri ->
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = "Selected Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp)
                    )
                    OutlinedTextField(
                        value = productDescription,
                        onValueChange = { productDescription = it },
                        label = { Text("Product Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(6.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = productPrice,
                            onValueChange = { productPrice = it },
                            label = { Text("Product Price") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        OutlinedTextField(
                            value = inventory,
                            onValueChange = { inventory = it },
                            label = { Text("Product Inventory") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = productBrand,
                            onValueChange = { productBrand = it },
                            label = { Text("Product Brand") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        )


                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            OutlinedTextField(
                                value = productCategory,
                                onValueChange = { },
                                label = { Text("Category") },
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Open Dropdown",
                                        modifier = Modifier.clickable { expanded = !expanded }
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categoryUiState.categories.forEach { category ->
                                    DropdownMenuItem(
                                        onClick = {
                                            productCategory = category.name
                                            expanded = false
                                        }
                                    ) {
                                        Text(
                                            text = category.name
                                        )
                                    }
                                }
                                Divider()
                                DropdownMenuItem(
                                    onClick = { isAddingCategory = true }
                                ) {
                                    Text(
                                        text = "Add New Category"
                                    )
                                }
                            }

                        }


                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


            }
            Button(
                onClick = {
                    scope.launch {
                        productViewModel.handleEvent(
                            UploadEvent.ProductUpload(
                                UploadProductRequest(
                                    brand = productBrand,
                                    category = productCategory,
                                    description = productDescription,
                                    images = uiState.imageUrls,
                                    inventory = inventory,
                                    name = productName,
                                    price = productPrice
                                )
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                enabled = networkState.value == NetworkConnectionState.Available
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(23.dp),
                        color = MaterialTheme.colors.onPrimary
                    )
                } else {
                    Text(
                        text = "Upload Product",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }

        }
    }
}




