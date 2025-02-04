package com.newton.mustmarket.features.merchant.store.mDataStore

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