package com.newton.mustmarket.features.merchant.create_store.presentation.view.merchant_input

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.newton.file_service.domain.model.ImageUploadState
import com.newton.mustmarket.core.sharedComposable.UploadProgressDialog
import com.newton.mustmarket.core.util.PermissionUtils
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest
import com.newton.mustmarket.features.merchant.create_store.presentation.event.MerchantEvent
import com.newton.mustmarket.features.merchant.create_store.presentation.viewModel.MerchantViewModel

@Composable
fun MerchantSignupScreen(
    viewModel: MerchantViewModel = hiltViewModel(),
    userId: String = "",
    onNavigateToMyStore: () -> Unit
) {

    val state by viewModel.createMerchantState.collectAsState()
    val uploadProgress by viewModel.uploadProgress.collectAsState()
    var bannerUri by remember { mutableStateOf<Uri?>(null) }
    var profileUri by remember { mutableStateOf<Uri?>(null) }
    var currentStep by remember { mutableIntStateOf(0) }
    var isUploadDialogVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val bannerImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        uri?.let { bannerUri = it }
    }

    val profileImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        uri?.let { profileUri = it }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {permissionResult ->
        if (permissionResult.values.all { it }) {
    } else {
        Toast.makeText(
            context,
            "Permission needed to select images",
            Toast.LENGTH_LONG
        ).show()
    }
    }

    fun onSelectImage(action: () -> Unit) {
        PermissionUtils.checkAndRequestPermission(
            context = context,
            permissionLauncher = permissionLauncher
        ) {
            action()
        }
    }

    LaunchedEffect(uploadProgress) {
        isUploadDialogVisible = when (uploadProgress) {
            is ImageUploadState.Progress -> true
            is ImageUploadState.Error -> false
             ImageUploadState.Loading -> true
            is ImageUploadState.MultipleImageSuccess -> false
            else -> false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToMyStore.collect {
            onNavigateToMyStore()
        }
    }

    state.error?.let { error ->
        LaunchedEffect(error) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.handleEvent(MerchantEvent.ClearError)
        }
    }

    state.success?.let { success ->
        LaunchedEffect(success) {
            Toast.makeText(context, success, Toast.LENGTH_LONG).show()
        }
    }

    UploadProgressDialog(
        isVisible = isUploadDialogVisible,
        progress = when (uploadProgress) {
            is ImageUploadState.Progress -> (uploadProgress as ImageUploadState.Progress).percentage / 100f
            else -> 0f
        },
        onDismissRequest = { isUploadDialogVisible = false }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        onSelectImage {
                            bannerImagePicker.launch("image/*")
                        }
                    },
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (bannerUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = context)
                            .data(bannerUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Banner Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Add Banner",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add Store Banner",
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 50.dp)
            ) {
                Card(
                    modifier = Modifier
                        .shadow(8.dp, CircleShape)
                        .clickable {
                            onSelectImage {
                                profileImagePicker.launch("image/*")
                            }
                        },
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = CircleShape
                ) {
                    if (profileUri != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = context)
                                .data(profileUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Add Profile",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(60.dp))

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            when(uploadProgress) {
                is ImageUploadState.Progress -> {
                    Text(
                        "Uploading: ${(uploadProgress as ImageUploadState.Progress).percentage}%",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                else -> {}
            }
        }

        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            }
        ) { step ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                when(step) {
                    0 -> BasicInfoSection(
                        name = state.merchantDetailsInput.merchantName,
                        onNameChange = {
                            viewModel.handleEvent(MerchantEvent.MerchantNameChanged(it))
                        }
                    )
                    1 -> LocationSection(
                        location = state.merchantDetailsInput.location,
                        onLocationChange = { location ->
                            viewModel.handleEvent(
                                MerchantEvent.ShopLocationChanged(location)
                            )
                        }
                    )
                    2 -> DescriptionSection(
                        description = state.merchantDetailsInput.shopDescription,
                        onDescriptionChange = {
                            viewModel.handleEvent(MerchantEvent.ShopDescriptionChanged(it))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentStep > 0) {
                Button(
                    onClick = { currentStep-- },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    enabled = !state.isLoading
                ) {
                    Text("Previous")
                }
            }

            Button(
                onClick = {
                    if (currentStep < 2) {
                        currentStep++
                    } else {
                       if (bannerUri != null && profileUri != null) {
                           viewModel.handleEvent(
                               MerchantEvent.BannerAndProfileUpload(
                                   context = context,
                                   bannerUri = bannerUri!!,
                                   profileUri = profileUri!!,
                                   createMerchantRequest = CreateMerchantRequest(
                                       name = state.merchantDetailsInput.merchantName,
                                       location = state.merchantDetailsInput.location,
                                       description = state.merchantDetailsInput.shopDescription,
                                       userId = userId,
                                       banner = "",
                                       profile = ""
                                   )
                               )
                           )
                       } else {
                           Toast.makeText(
                               context,
                               "Please select both banner and profile images",
                               Toast.LENGTH_LONG
                           ).show()
                       }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (currentStep > 0) 8.dp else 0.dp),
//                enabled = !state.isLoading &&
//                        (currentStep < 2),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                )
            ) {
                Text(if (currentStep < 2) "Next" else "Complete Setup")
            }
        }

        LinearProgressIndicator(
            progress = (currentStep + 1) / 3f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }

}