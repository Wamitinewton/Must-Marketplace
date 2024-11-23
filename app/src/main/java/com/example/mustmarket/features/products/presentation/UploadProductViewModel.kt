package com.example.mustmarket.features.products.presentation

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.home.domain.model.products.AllNetworkProduct
import com.example.mustmarket.features.products.domain.models.UploadData
import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.products.domain.usecases.ProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class UploadProductViewModel @Inject constructor(
    private val productUseCases: ProductUseCases
): ViewModel(){
    var toastMessage = mutableStateOf("")
        private set
    var isLoading = mutableStateOf(false)
        private set

    var singleImageData = mutableStateOf("")
        private set

    var imageListData = mutableStateOf(listOf(String()))
        private set

    var productData = mutableStateOf(UploadData())
        private set

    fun uploadSingleImage(uri: Uri,context: Context){
        viewModelScope.launch{
            productUseCases.uploadImage(uriToFile(context,uri)).collect{ response->
                when(response){
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        singleImageData.value = response.data?.data!!
                        isLoading.value = false
                    }
                    is Resource.Error -> {
                        toastMessage.value =  response.data?.data?: "Unable to upload your Image"
                    }
                }
            }
        }
    }

    fun uploadImageList(context: Context,uri: List<Uri>){
        viewModelScope.launch{
            productUseCases.uploadImageList(
                uri.map {
                    uriToFile(context,it)
                }
            ).collect{response->
                when(response){
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        imageListData.value = response.data?.data!!
                        isLoading.value = false
                    }
                    is Resource.Error -> {}
                }
            }
        }
    }


    fun addProduct(product: UploadProductRequest){
        viewModelScope.launch{
            productUseCases.addProduct(product).collect{response->
                when(response){
                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                    is Resource.Success -> {
                        productData.value = response.data?.data!!
                        isLoading.value = false
                    }
                    is Resource.Error -> {}
                }

            }
        }
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, uri.path?:"temp_${System.currentTimeMillis()}.jpg")
        val outputStream: OutputStream = file.outputStream()
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}