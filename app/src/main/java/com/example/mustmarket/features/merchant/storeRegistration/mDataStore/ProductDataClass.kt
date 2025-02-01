package com.example.mustmarket.features.merchant.storeRegistration.mDataStore

data class Product(
    val id: String,
    val name: String,
    val price: String,
    val imageUri: String
)

data class StoreProfile(
    val MerchantId: String,
    val storeName: String?,
    val storeLogoUrl: String?
)