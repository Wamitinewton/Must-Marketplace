package com.newton.mustmarket.features.get_products.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel
@Inject
constructor(

) : ViewModel() {

    var details by mutableStateOf<NetworkProduct?>(null)
        private set

    var productList by mutableStateOf<List<NetworkProduct>?>(null)
        private set


    fun addDetails(newsDetails: NetworkProduct) {
        details = newsDetails
    }

    fun addProductList(products: List<NetworkProduct>) {
        productList = products
    }
}