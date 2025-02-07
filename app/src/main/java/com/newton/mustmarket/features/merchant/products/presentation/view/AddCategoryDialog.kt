package com.newton.mustmarket.features.merchant.products.presentation.view

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.newton.mustmarket.core.sharedComposable.LoadingState
import com.newton.mustmarket.features.products.presentation.state.ProductCategoryViewModelState

@Composable
fun AddCategoryDialog(
    onDismissRequest: (Boolean) -> Unit,
    isAddingCategory: Boolean,
    newCategoryName: String,
    onCategoryNameChange: (String) -> Unit,
    newCategoryImageUri: Uri?,
    categoryUiState: ProductCategoryViewModelState,
    onUploadClick: () -> Unit,
    onChooseImageClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest(isAddingCategory) }
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
                    onValueChange = onCategoryNameChange,

                    label = {
                        Text(
                            text = "Category Name"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Button(
                    onClick = onChooseImageClick,
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
                    if (categoryUiState.addCategoryState.isLoading) {
                        LoadingState()
                    } else {
                        Button(
                            onClick = onUploadClick,
                            modifier = Modifier.weight(1f),
                            enabled = !categoryUiState.addCategoryState.isLoading && (newCategoryName.isNotBlank() && newCategoryImageUri != null),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Upload"
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = onCancelClick,
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