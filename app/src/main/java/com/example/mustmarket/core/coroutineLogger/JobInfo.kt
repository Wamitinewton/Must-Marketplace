package com.example.mustmarket.core.coroutineLogger

import kotlinx.coroutines.Job

data class JobInfo(
    val job: Job,
    val stackTrace: String,
    val startTimeMillis: Long,
    val tag: String
)
