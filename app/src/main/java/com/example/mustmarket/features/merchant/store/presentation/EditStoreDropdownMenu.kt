package com.example.mustmarket.features.merchant.store.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mustmarket.core.sharedComposable.ButtonLoading
import com.example.mustmarket.core.sharedComposable.DefaultTextInput
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun EditStoreDropdownMenu(
    storeName: String,
    storeDescription: String,
    storeImageUri: Uri?,
    onUpdateStoreDetails: ((String, String, Uri?) -> Unit)?
) {
    var expanded by remember { mutableStateOf(false) }
    var updatedStoreName by remember { mutableStateOf(storeName) }
    var updatedStoreDescription by remember { mutableStateOf(storeDescription) }
    var updatedStoreImageUri by remember { mutableStateOf<Uri?>(storeImageUri) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        updatedStoreImageUri = uri
    }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Store",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    ThemeUtils.AppColors.Surface.themed(),
                )
                .padding(16.dp)
                .width(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Edit Your Stall Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = ThemeUtils.AppColors.Text.themed()
                )

                Spacer(modifier = Modifier.height(12.dp))

                DefaultTextInput(
                    onInputChanged = { newStoreName ->
                        updatedStoreName = newStoreName.replaceFirstChar { it.uppercase() }
                                     },
                    inputText = updatedStoreName,
                    name = "Change Stall Name",
                    myKeyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction  = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                DefaultTextInput(
                    onInputChanged = {
                        updatedStoreDescription = it.capitalize()
                                     },
                    inputText = updatedStoreDescription,
                    name = "Update Stall Description",
                    myKeyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Store Logo Upload
                updatedStoreImageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Updated Store Logo",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                ThemeUtils.AppColors.Surface.themed()
                            )
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                } ?: Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            ThemeUtils.AppColors.Surface.themed(),
                            shape = CircleShape
                        )
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Logo",
                        tint = ThemeUtils.AppColors.Primary,
                        modifier = Modifier
                            .size(30.dp)

                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                ButtonLoading(
                    name = "Update Stall Details",
                    isLoading = false,
                    enabled = true,
                    onClicked = {
                        onUpdateStoreDetails?.invoke(updatedStoreName, updatedStoreDescription, updatedStoreImageUri)
                        expanded = false  // Close dropdown after update
                    }
                )
            }
        }
    }
}