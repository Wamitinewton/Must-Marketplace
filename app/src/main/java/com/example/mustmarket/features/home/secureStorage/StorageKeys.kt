package com.example.mustmarket.features.home.secureStorage

object StorageKeys {
    const val PREF_FILE_NAME = "secure_product_preferences"
    const val PREF_LAST_UPDATE = "last_update_timestamp"
    const val CACHE_DURATION = 24
    const val BATCH_SIZE = 100
    const val MAX_RETRY_ATTEMPTS = 3
    const val INITIAL_RETRY_DELAY = 100L
    const val RETRY_FACTOR = 2.0
}