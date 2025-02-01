package com.example.mustmarket.features.merchant.storeRegistration.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.core.sharedComposable.ButtonLoading
import com.example.mustmarket.core.sharedComposable.DefaultTextInput
import com.example.mustmarket.features.merchant.storeRegistration.mDataStore.MerchantPreferences
import com.example.mustmarket.features.merchant.storeRegistration.viewModel.MerchantViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@SuppressLint("SuspiciousIndentation")
@Composable
fun RegisterStoreScreen(
    navController: NavController,
    storeNameError: String = "",
    businessTypeError: String = "",
    onRegister: ((String, String, String, String, String, Uri?) -> Unit)? = null,
    merchantViewModel: MerchantViewModel = viewModel()
){
    var storeName by remember {
        mutableStateOf("")
    }
    var businessType by remember {
        mutableStateOf("")
    }

    var storeLocation by remember {
        mutableStateOf("")
    }

    var businessPhone by remember {
        mutableStateOf("")
    }

    var businessDescription by remember {
        mutableStateOf("")
    }
    var storeImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current

    val businessTypes = listOf(
        "Single Seller",
        "Hawking",
        "Wholesale",
        "Restaurant",
        "Retail",
        "Supermarket",
        "Groceries",
        "Pharmacy",
        "Salon",
        "Barber",
        "Beauty",
        "Transport",
        "Logistics",
        "Housing",
        "Electronics",
        "Clothing",
        "Service",
        "Other"
    )

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
                        "Become a Seller",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = ThemeUtils.AppColors.Text.themed()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = ThemeUtils.AppColors.Surface.themed(),
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultTextInput(
                onInputChanged = {
                    storeName = it
                },
                inputText = storeName,
                name = "Stall Name",
                errorMessage = storeNameError,
                myKeyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            var expanded by remember {
                mutableStateOf(false)
            }

            Box {
                DefaultTextInput(
                    onInputChanged = {
                        businessType = it
                    },
                    inputText = businessType,
                    name = "Business Type",
                    errorMessage = businessTypeError,
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier,
                            onClick = {
                                expanded = true
                            }
                        ) {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select Business Type",
                                tint = MaterialTheme.colors.primarySurface,
                                modifier = Modifier
                                    .size(44.dp)
                            )
                        }
                    },

                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    businessTypes.forEach { type ->
                        DropdownMenuItem(
                            content = {
                                Text(type)
                            },
                            onClick = {
                                businessType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            DefaultTextInput(
                onInputChanged = {
                    businessPhone = it
                },
                inputText = businessPhone,
                name = "Business Phone",
                errorMessage = "",
                myKeyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            DefaultTextInput(
                onInputChanged = {
                    businessDescription = it
                },
                inputText = businessDescription,
                name = "Business Description",
                errorMessage = "",
                myKeyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default
                ),
                maxLines = 5,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            DefaultTextInput(
                onInputChanged = {
                    storeLocation = it
                },
                inputText = storeLocation,
                name = "Stall Location",
                errorMessage = "",
                myKeyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            storeImageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Stall Logo",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ButtonLoading(
                name = "Upload Stall Logo",
                isLoading = false,
                enabled = true,
                onClicked = {
                    imagePickerLaunch.launch("image/*")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ButtonLoading(
                name = "Create Your Stall",
                isLoading = false,
                enabled = true,
                onClicked = {
                    val merchantId = MerchantPreferences.getMerchantId(context)
                        onRegister?.invoke(
                        storeName,
                        businessType,
                        storeLocation,
                        businessPhone,
                        businessDescription,
                        storeImageUri
                    )
                    merchantViewModel.setMerchantStatus(true, "sample_merchant_id")// remove this code when it comes to production
                    navController.navigate(
                        Screen.MerchantStore.createRoute(merchantId.toString())
                    )
                }
            )
        }
    }
}