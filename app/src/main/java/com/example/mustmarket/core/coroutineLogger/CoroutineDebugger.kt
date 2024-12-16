package com.example.mustmarket.core.coroutineLogger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

class CoroutineDebugger {
    private val activeCoroutines = ConcurrentHashMap<String, JobInfo>()
    private val coroutineCounter = AtomicInteger(0)
    private val timeStamp = System.currentTimeMillis()


    companion object {
        private var instance: CoroutineDebugger? = null
        private var isDebugMode = false

        fun getInstance(): CoroutineDebugger {
            return instance ?: synchronized(this) {
                instance ?: CoroutineDebugger().also { instance = it }
            }
        }

        fun enableDebugging() {
            isDebugMode = true
        }
    }

    fun <T> launchTracked(
        scope: CoroutineScope,
        tag: String = "Untagged",
        context: CoroutineContext? = null,
        block: suspend CoroutineScope.() -> T
    ): Job {
        if (!isDebugMode) {
            return if (context != null) {
                scope.launch(context) { block() }
            } else {
                scope.launch { block() }
            }
        }

        val coroutineId = "${tag}_${coroutineCounter.incrementAndGet()}"
        val stackTrace = Exception().stackTraceToString()

        return if (context != null) {
            scope.launch(context) {
                trackCoroutine(coroutineId, tag, stackTrace, this.coroutineContext[Job]!!)
                try {
                    block()
                } finally {
                    untrackCoroutine(coroutineId)
                }
            }
        } else {
            scope.launch {
                trackCoroutine(coroutineId, tag, stackTrace, this.coroutineContext[Job]!!)
                try {
                    block()
                } finally {
                    untrackCoroutine(coroutineId)
                }
            }
        }
    }

    private fun trackCoroutine(coroutineId: String, tag: String, stackTrace: String, job: Job) {
        activeCoroutines[coroutineId] = JobInfo(
            job = job,
            stackTrace = stackTrace,
            startTimeMillis = System.currentTimeMillis(),
            tag = tag
        )
        logCoroutineStart(coroutineId)
    }

    private fun untrackCoroutine(coroutineId: String) {
        activeCoroutines.remove(coroutineId)?.let { jobInfo ->
            logCoroutineEnd(coroutineId, jobInfo)
        }
    }

    fun getActiveCoroutinesInfo(): List<CoroutineInfo> {
        return activeCoroutines.map { (id, jobInfo) ->
            CoroutineInfo(
                id = id,
                tag = jobInfo.tag,
                duration = System.currentTimeMillis() - jobInfo.startTimeMillis,
                isActive = jobInfo.job.isActive,
                isCancelled = jobInfo.job.isCancelled,
                isCompleted = jobInfo.job.isCompleted,
                stackTrace = jobInfo.stackTrace
            )
        }
    }

    fun cancelAllCoroutines() {
        activeCoroutines.forEach { (_, jobInfo) ->
            jobInfo.job.cancel()
        }
    }

    private fun logCoroutineStart(coroutineId: String) {
        val jobInfo = activeCoroutines[coroutineId]
        println("🚀 Coroutine Started $timeStamp: $coroutineId (${jobInfo?.tag})")
        println("📍 Created at $timeStamp:\n${jobInfo?.stackTrace}")
    }

    private fun logCoroutineEnd(coroutineId: String, jobInfo: JobInfo) {
        val duration = System.currentTimeMillis() - jobInfo.startTimeMillis
        println("✅ Coroutine Ended: $coroutineId (${jobInfo.tag})")
        println("⏱️ Duration: ${duration}ms")
        if (duration > 1000) {
            println("⚠️ Warning: Coroutine took longer than 1 second to complete!")
        }
    }


}