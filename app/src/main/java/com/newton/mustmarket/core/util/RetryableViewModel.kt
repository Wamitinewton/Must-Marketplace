package com.newton.mustmarket.core.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class RetryableViewModel: ViewModel() {
    private val _retryTrigger = MutableStateFlow(0)
    val retryTrigger: StateFlow<Int> = _retryTrigger

    fun retry() {
        _retryTrigger.value += 1
    }
    abstract fun handleRetry()
}