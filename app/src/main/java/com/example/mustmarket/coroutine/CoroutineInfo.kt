package com.example.mustmarket.coroutine

data class CoroutineInfo(
    val id: String,
    val tag: String,
    val duration: Long,
    val isActive: Boolean,
    val isCancelled: Boolean,
    val isCompleted: Boolean,
    val stackTrace: String
)
