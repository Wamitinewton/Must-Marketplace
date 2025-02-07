package com.newton.mustmarket.features.merchant.products.presentation.view

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.newton.mustmarket.core.sharedComposable.LoadingAnimationType
import com.newton.mustmarket.core.sharedComposable.LoadingState
import com.newton.mustmarket.core.sharedComposable.ProductInputFields
import com.newton.mustmarket.features.products.presentation.state.ProductCategoryViewModelState

@Composable
fun ProductsInputForm(
    onChooseProductImages: () -> Unit,
    selectedImages: List<Uri>,
    onProductNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onPriceChanged: (Int) -> Unit,
    onInventoryChanged: (Int) -> Unit,
    onBrandChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    padding: PaddingValues,
    productName: String,
    productDescription: String,
    productPrice: Int,
    productInventory: Int,
    productBrand: String,
    productCategory: String,
    productCategoryUiState: ProductCategoryViewModelState,
    onCategoryClicked: (String) -> Unit,
    onDropCategory: () -> Unit,
    isImageUploading: Boolean,
) {
    Column(

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
                    .clickable(
                        onClick = onChooseProductImages
                    ),
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
                    if (isImageUploading) {
                        Column {
                            Text(
                                "Uploading Images...",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LoadingState(type = LoadingAnimationType.PULSING_DOTS)
                        }
                    } else {
                        AsyncImage(
                            model = selectedImages.firstOrNull(),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProductInputFields(
                    onInputChanged = onProductNameChanged,
                    inputText = productName,
                    labelName = "Product Name",
                    keyboardType = KeyboardType.Text,
                    isDescription = false
                )
                ProductInputFields(
                    onInputChanged = onDescriptionChanged,
                    inputText = productDescription,
                    labelName = "Product Description",
                    keyboardType = KeyboardType.Text,
                    isDescription = true
                )


                ProductInputFields(
                    inputText = productPrice.toString(),
                    onInputChanged = { newValueStr ->
                        val newValue = newValueStr.toIntOrNull() ?: 0
                        onPriceChanged(newValue)

                    },
                    labelName = "Product Price",
                    keyboardType = KeyboardType.Number
                )
                ProductInputFields(
                    inputText = productInventory.toString(),
                    onInputChanged = { newValueStr ->
                        val newValue = newValueStr.toIntOrNull() ?: 0
                        onInventoryChanged(newValue)

                    },
                    labelName = "Product Inventory",
                    keyboardType = KeyboardType.Number
                )

                ProductInputFields(
                    inputText = productBrand,
                    onInputChanged = onBrandChanged,
                    labelName = "Product Brand",
                    keyboardType = KeyboardType.Text
                )


                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = productCategory,
                        onValueChange = onCategoryChanged,
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
                        productCategoryUiState.categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    onCategoryClicked(category.name)
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
                            onClick = onDropCategory
                        ) {
                            Text(
                                text = "Add New Category"
                            )
                        }
                    }

                }


            }
        }


    }
}