package com.example.mustmarket.features.merchant.storeRegistration.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mustmarket.core.sharedComposable.DefaultTextInput
import com.example.mustmarket.navigation.Screen

@Composable
fun RegisterStoreScreen(
    navController: NavController
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
        "Service",
        "Other"
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Register Your Store")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            DefaultTextInput(
                onInputChanged = {
                    storeName = it
                },
                inputText = storeName,
                name = "Store Name",
                //errorMessage = TODO(),
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    //errorMessage = TODO(),
//                    readOnly = true,
//                    trailingIcon = {
//                        IconButton(
//                            onClick = {
//                                expanded = true
//                            }
//                        ){
//                            Icon(
//                                Icons.Default.ArrowDropDown,
//                                contentDescription = "Select Business Type"
//                            )
//                        }
//                    }
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
        }

    }
}