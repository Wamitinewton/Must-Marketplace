package com.example.mustmarket.features.merchant.storeRegistration.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun StoreProfileScreen(
    storeName: String? = "Change Stall Name",
    storeDescription: String? = "Initial Description",
    phoneNumber: String? = "07123456789",
    storeImageUri: Uri? = null,
    onUpdateStoreDetails: ((String, String, String, Uri?) -> Unit)? = null,
    navController: NavController
) {

    val focusManager = LocalFocusManager.current

    var updatedStoreName by remember { mutableStateOf(storeName ?: "") }
    var updatedStoreDescription by remember { mutableStateOf(storeDescription ?: "") }
    var updatedPhoneNumber by remember { mutableStateOf(phoneNumber ?: "") }
    var updatedStoreImageUri by remember { mutableStateOf<Uri?>(storeImageUri) }

    var isEditingName by remember { mutableStateOf(false) }
    var isEditingDescription by remember { mutableStateOf(false) }
    var isEditingPhone by remember { mutableStateOf(false) }

    var showEditDialog by remember {
        mutableStateOf(false)
    }
    var currentlyEditingField: String? by remember {
        mutableStateOf(null)
    }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        updatedStoreImageUri = uri
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = ThemeUtils.AppColors.Text.themed(),
                        fontWeight = FontWeight.Bold,
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
        }
    ){ padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Profile Image Section
            Box(
                modifier = Modifier
                    .size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape
                        )
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.surface),
                    contentAlignment = Alignment.Center
                ) {
                    if (updatedStoreImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(updatedStoreImageUri),
                            contentDescription = "Store Logo",
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Profile",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }

                // Floating camera icon
                Surface(
                    modifier = Modifier
                        .clickable(
                            onClick = { imagePickerLauncher.launch("image/*") }
                        )
                        .size(32.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colors.surface,
                    border = BorderStroke(2.dp, MaterialTheme.colors.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Update Photo",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Store Name Section
            ProfileField(
                icon = Icons.Default.Store,
                label = "Store Name",
                value = updatedStoreName,
                isEditing = isEditingName,
                onEditClick = {
                    isEditingName = true
                    currentlyEditingField = "name"
                    focusManager.clearFocus()
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onValueChange = { updatedStoreName = it },
                onDone = {
                    isEditingName = false
                    onUpdateStoreDetails?.invoke(
                        updatedStoreName,
                        updatedStoreDescription,
                        updatedPhoneNumber,
                        updatedStoreImageUri
                    )
                }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Description Section
            ProfileField(
                icon = Icons.Default.Info,
                label = "Description",
                value = updatedStoreDescription,
                isEditing = isEditingDescription,
                onEditClick = {
                    isEditingDescription = true
                    currentlyEditingField = "description"
                    focusManager.clearFocus()
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onValueChange = { updatedStoreDescription = it },
                onDone = {
                    isEditingDescription = false
                    onUpdateStoreDetails?.invoke(
                        updatedStoreName,
                        updatedStoreDescription,
                        updatedPhoneNumber,
                        updatedStoreImageUri
                    )
                }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Phone Section
            ProfileField(
                icon = Icons.Default.Phone,
                label = "Phone Number",
                value = updatedPhoneNumber,
                isEditing = isEditingPhone,
                onEditClick = {
                    isEditingPhone = true
                    currentlyEditingField = "phone"
                    focusManager.clearFocus()
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onValueChange = { updatedPhoneNumber = it },
                onDone = {
                    isEditingPhone = false
                    onUpdateStoreDetails?.invoke(
                        updatedStoreName,
                        updatedStoreDescription,
                        updatedPhoneNumber,
                        updatedStoreImageUri
                    )
                },
                keyboardType = KeyboardType.Phone
            )
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// Store input field
///////////////////////////////////////////////////////////////////////////

@Composable
private fun ProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //leading icon
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ThemeUtils.AppColors.Primary,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {

            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ThemeUtils.AppColors.Text.themed()
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (isEditing) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),

                    singleLine = true,
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.body1,
                    color = ThemeUtils.AppColors.Text.themed()
                )
            }
        }

        if (isEditing) {
            IconButton(onClick = onDone) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "save",
                    tint = ThemeUtils.AppColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = ThemeUtils.AppColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// editing diaglog for merchant store profile
///////////////////////////////////////////////////////////////////////////

@Composable
private fun EditDialog(
    fieldName: String?,
    initialValue: String,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
){
    var editedValue by remember{
        mutableStateOf(initialValue)
    }

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = "Edit ${fieldName?.replaceFirstChar { it.uppercase() }}",
                fontWeight = FontWeight.Bold,
                color = ThemeUtils.AppColors.Text.themed()
            )
        },
        text = {
            TextField(
                value = editedValue,
                onValueChange = {
                    editedValue = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(editedValue) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    )
}