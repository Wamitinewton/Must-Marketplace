package com.example.mustmarket.core.retryConfig

import com.example.mustmarket.core.util.RepositoryError

data class RetryConfig(
    val maxAttempts: Int = 3,
    val initialDelayMillis: Long = 1000,
    val factor: Double = 2.0,
    val shouldRetry: (Exception) -> Boolean = { it !is RepositoryError }
) {
    companion object {
        val DEFAULT = RetryConfig()

        val AGGRESSIVE = RetryConfig(
            maxAttempts = 5,
            initialDelayMillis = 500,
            factor = 1.5,
        )

        val CONSERVATIVE = RetryConfig(
            maxAttempts = 3,
            initialDelayMillis = 2000,
            factor = 2.0,
        )
    }
}
