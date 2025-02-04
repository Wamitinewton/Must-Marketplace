package com.newton.mustmarket.core.coroutineLogger

data class CoroutineInfo(
    val id: String,
    val tag: String,
    val duration: Long,
    val isActive: Boolean,
    val isCancelled: Boolean,
    val isCompleted: Boolean,
    val stackTrace: String
)
