package com.example.mustmarket.features.merchant.products.presentation.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mustmarket.core.networkManager.NetworkConnectionState
import com.example.mustmarket.core.networkManager.rememberConnectivityState
<<<<<<< HEAD
import com.example.mustmarket.database.dao.UserDao
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
=======
import com.example.mustmarket.features.auth.data.datastore.UserStoreManager
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import com.example.mustmarket.features.home.presentation.event.CategoryEvent
import com.example.mustmarket.features.home.presentation.event.HomeScreenEvent
import com.example.mustmarket.features.home.presentation.viewmodels.AllProductsViewModel
import com.example.mustmarket.features.home.presentation.viewmodels.ProductCategoryViewModel
import com.example.mustmarket.features.merchant.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.merchant.products.presentation.event.UploadEvent
import com.example.mustmarket.features.merchant.products.presentation.viewModel.UploadProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
<<<<<<< HEAD
import timber.log.Timber
=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

@Composable
fun UploadProducts(
    productViewModel: UploadProductViewModel = hiltViewModel(),
    categoryViewModel: ProductCategoryViewModel = hiltViewModel(),
<<<<<<< HEAD
    allProductsViewModel: AllProductsViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
=======
    userStoreManager: UserStoreManager,
    allProductsViewModel: AllProductsViewModel = hiltViewModel()
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
) {
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
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
<<<<<<< HEAD
    val user by loginViewModel.loggedInUser.collectAsState()

=======

    val userId = userStoreManager.fetchUserData()
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

    val categoryLauncherPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> newCategoryImageUri = uri }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.let {
            selectedImages = it
        }
    }
    val enabled = !uiState.isLoading && !uiState.uploadMultipleImagesState.isLoading

    LaunchedEffect(
        categoryUiState.addCategoryState.successMessage,
        categoryUiState.addCategoryState.errorMessage,
        uiState
    ) {
        categoryUiState.addCategoryState.successMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Long
            )
        }
        categoryUiState.addCategoryState.errorMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Long
            )
        }

        uiState.error?.let { error ->
            Log.d("Errrrrrooooor", error)
            scaffoldState.snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            productViewModel.handleEvent(UploadEvent.ClearError)
        }

        uiState.success?.let { success ->

            scaffoldState.snackbarHostState.showSnackbar(
                message = success,
                duration = SnackbarDuration.Short
            )
        }

        uiState.uploadMultipleImagesState.success?.let { success ->

            scaffoldState.snackbarHostState.showSnackbar(
                message = success,
                duration = SnackbarDuration.Short
            )
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

    if (isAddingCategory) {
        AddCategoryDialog(
            onDismissRequest = { isAddingCategory = false },
            isAddingCategory = isAddingCategory,
            newCategoryName = newCategoryName,
            onCategoryNameChange = { newCategoryName = it },
            newCategoryImageUri = newCategoryImageUri,
            categoryUiState = categoryUiState,
            onUploadClick = {
                categoryViewModel.onCategoryEvent(
                    CategoryEvent.CategoryUploadEvent(
                        newCategoryImageUri!!,
                        newCategoryName,
                        context
                    )
                )
                newCategoryName = ""
                newCategoryImageUri = null
            },
            onChooseImageClick = { categoryLauncherPicker.launch("image/*") },
            onCancelClick = {
                isAddingCategory = false
                newCategoryName = ""
                newCategoryImageUri = null
            }
        )
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
<<<<<<< HEAD
                    Timber.tag("Errrrrrooooor").d(snackbarData.message)
=======
                    Log.d("Errrrrrooooor", snackbarData.message)
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
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
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Text(
                                "Add New Product",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Box(
                                contentAlignment = Alignment.TopEnd,
                                modifier = Modifier.size(48.dp)
                            ) {
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shop,
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.primary,
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .offset(x = (-4).dp, y = 4.dp)
                                        .size(18.dp)
                                        .background(
                                            color = MaterialTheme.colors.primary,
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = MaterialTheme.colors.surface,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "3",
                                        style = MaterialTheme.typography.caption.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colors.onSecondary
                                        ),
                                        modifier = Modifier.padding(2.dp)
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 60.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProductsInputForm(
                onChooseProductImages = {
                    launcher.launch("image/*")
                },
                selectedImages = selectedImages,
                onProductNameChanged = {
                    productViewModel.handleEvent(
                        UploadEvent.ProductNameChanged(
                            name = it
                        )
                    )
                },
                onDescriptionChanged = {
                    productViewModel.handleEvent(
                        UploadEvent.ProductDescriptionChanged(
                            description = it
                        )
                    )
                },
                onPriceChanged = {
                    productViewModel.handleEvent(
                        UploadEvent.ProductPriceChanged(
                            price = it
                        )
                    )
                },
                onInventoryChanged = {
                    productViewModel.handleEvent(
                        UploadEvent.ProductInventoryChanged(
                            inventory = it
                        )
                    )
                },
                onBrandChanged = {
                    productViewModel.handleEvent(
                        UploadEvent.ProductBrandChanged(
                            brand = it
                        )
                    )
                },
                onCategoryChanged = {
                    productViewModel.handleEvent(
                        UploadEvent.ProductCategoryChanged(
                            category = it
                        )
                    )
                },
                padding = padding,
                productName = uiState.productInput.productName,
                productDescription = uiState.productInput.productDescription,
                productPrice = uiState.productInput.productPrice ?: 0,
                productInventory = uiState.productInput.productInventory ?: 0,
                productBrand = uiState.productInput.productBrand,
                productCategory = uiState.productInput.productCategory,
                productCategoryUiState = categoryUiState,
                onCategoryClicked = { selectedCategory ->
                    productViewModel.handleEvent(
                        UploadEvent.ProductCategoryChanged(
                            category = selectedCategory
                        )
                    )

                },
                onDropCategory = {
                    isAddingCategory = true
                },
                isImageUploading = uiState.uploadMultipleImagesState.isLoading
            )
            Button(
                enabled = enabled,
                onClick = {
                    scope.launch {
                        productViewModel.handleEvent(
                            UploadEvent.MultipleImagesUpload(
                                selectedImages,
                                context,
                                UploadProductRequest(
                                    brand = uiState.productInput.productBrand,
                                    category = uiState.productInput.productCategory,
                                    description = uiState.productInput.productDescription,
                                    images = uiState.uploadMultipleImagesState.multipleImagesUrl,
                                    inventory = uiState.productInput.productInventory,
                                    name = uiState.productInput.productName,
                                    price = uiState.productInput.productPrice,
<<<<<<< HEAD
                                    userId = user!!.id.toString()
=======
                                    userId = userId?.id!!
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
                                )
                            )
                        )
                        allProductsViewModel.onProductEvent(
                            HomeScreenEvent.Refresh
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(15.dp)
                    ),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
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
