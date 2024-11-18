package com.example.mustmarket.core.retryConfig

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetryUtil @Inject constructor() {
    suspend fun <T> executeWithRetry(
        config: RetryConfig = RetryConfig.DEFAULT,
        block: suspend () -> T
    ): T {
        var currentDelay = config.initialDelayMillis
        var lastException: Exception? = null

        repeat(config.maxAttempts - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e

                if (!config.shouldRetry(e)) {
                    throw e
                }
                delay(currentDelay)
                currentDelay = (currentDelay * config.factor).toLong()
            }
        }
        return try {
            block()
        } catch (e: Exception) {
            throw lastException ?: e
        }
    }
}