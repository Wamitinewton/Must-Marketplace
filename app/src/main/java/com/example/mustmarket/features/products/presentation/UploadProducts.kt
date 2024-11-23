package com.example.mustmarket.features.products.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mustmarket.R
import com.example.mustmarket.features.products.domain.models.UploadProductRequest

@Composable
fun UploadProducts(
    productViewModel: UploadProductViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<List<Uri>?>(emptyList())
    }
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uri: List<Uri>? ->
        if (uri != null) {
            imageUri = uri
            productViewModel.uploadImageList(context, uri)
        }
    }
    var postDescription by remember { mutableStateOf("") }
    var imageList by remember { mutableStateOf(productViewModel.imageListData.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TransparentHintTextField(
            text = postDescription,
            hint = "Post description",
            onValueChange = {
                postDescription = it
            },
            onFocusChange = {
                it.isFocused
            },

            )

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            LaunchedEffect(key1 = imageUri) {
                if (imageUri!!.isNotEmpty()) {
                    (imageUri as Iterable<Uri>).forEach {
                        bitmap = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                            MediaStore.Images
                                .Media.getBitmap(context.contentResolver, it)
                        } else {
                            val source = ImageDecoder
                                .createSource(context.contentResolver, it)
                            ImageDecoder.decodeBitmap(source)
                        }
                    }

                }
            }
            if (bitmap != null) {
                Image(
                    painter = rememberAsyncImagePainter(bitmap),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { launcher.launch("image/*") }
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                if (imageList.isNotEmpty()) {
                    Image(painter = rememberAsyncImagePainter(imageList[0]),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { launcher.launch("image/*") }
                            .size(60.dp),
                        contentScale = ContentScale.Crop)
                } else {
                    Image(painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context).data(R.mipmap.ic_launcher_round)
                            .build()
                    ),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { launcher.launch("image/*") }
                            .size(60.dp),
                        contentScale = ContentScale.Crop)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            onClick = {
                productViewModel.addProduct(
                    UploadProductRequest(
                        brand = "HelloWorld",
                        category = "laptops",
                        description = "Logic test",
                        images = imageList,
                        inventory = "2",
                        name = "HelloworldTest",
                        price = "100"
                    )
                )
//                navController.popBackStack()
//                navController.navigate(Screens.HomeScreen.route)
            },
        ) {
            Text(text = "post", style = MaterialTheme.typography.subtitle1)
        }
    }
}


@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                },

            )
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
                fontSize = 20.sp,
            )
        }
    }
}

