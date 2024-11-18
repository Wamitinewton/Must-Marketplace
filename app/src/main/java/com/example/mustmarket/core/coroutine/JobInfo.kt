package com.example.mustmarket.core.coroutine

import kotlinx.coroutines.Job

data class JobInfo(
    val job: Job,
    val stackTrace: String,
    val startTimeMillis: Long,
    val tag: String
)
