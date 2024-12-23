package com.example.mustmarket.features.home.workManager

data class ProductSyncState(
    val lastSyncTimeStamp: Long = 0L,
    val isSyncing: Boolean = false,
    val syncError: String? = null
)
