package com.example.mustmarket.core.threads

import com.example.mustmarket.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiThreadingActivity @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val exceptionHandler: CoroutineExceptionHandler
) {
    private val operationMutexes = ConcurrentHashMap<OperationType, Mutex>()
    private val activeOperations = ConcurrentHashMap<String, Job>()
    private val mutex = Mutex()

    init {
        OperationType.entries.forEach { type ->
            operationMutexes[type] = Mutex()
        }
        Timber.d("MultiThreadingActivity initialized with operation types: ${OperationType.entries}")

    }

    fun <T> executeIsolatedThread(
        operationType: OperationType,
        timeOuts: Long = DEFAULT_TIMEOUT,
        maxRetries: Int = DEFAULT_RETRIES,
        block: suspend () -> T
    ): Flow<T> = flow {
        val operationId = "${operationType.name}_${System.currentTimeMillis()}"
        Timber.d("Starting isolated thread for operation: $operationType with ID: $operationId")
        try {
            mutex.withLock {
                cleanupCompleteOperations()
                Timber.d("Cleaned up completed or cancelled operations.")
            }

            val result: T = withContext(ioDispatcher + exceptionHandler) {
                withTimeout(timeOuts) {
                    operationMutexes[operationType]?.withLock {
                        Timber.d("Executing block for operation: $operationType")
                        val result = retryWithExponentialBackoff(
                            maxRetries = maxRetries,
                            initialDelay = INITIAL_DELAY
                        ) {
                            block()
                        }
                        // Store the job for tracking purposes
                        activeOperations[operationId] = launch { }
                        Timber.d("Operation completed successfully for ID: $operationId")
                        result
                    }!!
                }
            }
            emit(result)
        } catch (e: TimeoutCancellationException) {
            Timber.e(e, "Timeout occurred for operation: $operationType with ID: $operationId")
            throw e
        } catch (e: Exception) {
            Timber.e(e, "Exception occurred for operation: $operationType with ID: $operationId")
            throw e
        } finally {
            mutex.withLock {
                activeOperations.remove(operationId)
                Timber.d("Removed operation from active operations: $operationId")
            }
        }
    }.buffer(Channel.BUFFERED)
        .flowOn(ioDispatcher)
        .catch { exception ->
            Timber.e(exception, "Flow encountered an exception for operation: $operationType")
            throw exception
        }

    fun <T> executeParallelOperations(
        operations: List<suspend () -> T>,
        timeOuts: Long = DEFAULT_TIMEOUT,
        maxRetries: Int = DEFAULT_RETRIES
    ): Flow<List<T>> = flow {
        val results = withContext(ioDispatcher) {
            operations.map { operation ->
                async {
                    withTimeout(timeOuts) {
                        retryWithExponentialBackoff(
                            maxRetries = maxRetries,
                            initialDelay = INITIAL_DELAY
                        ) {
                            operation()
                        }
                    }
                }
            }.awaitAll()
        }
        emit(results)
    }.buffer(Channel.BUFFERED)
        .flowOn(ioDispatcher)
        .catch { exception ->
            emit(emptyList())
            throw exception
        }

    private fun cleanupCompleteOperations() {
        val completedOperations =
            activeOperations.entries.filter { (_, job) -> job.isCompleted || job.isCancelled }
        completedOperations.forEach { (id, _) ->
            Timber.d("Cleaning up operation with ID: $id")
        }
        activeOperations.entries.removeAll(completedOperations.toSet())
    }

    private suspend fun <T> retryWithExponentialBackoff(
        maxRetries: Int,
        initialDelay: Long,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(maxRetries) { attempt ->
            try {
                Timber.d("Attempt $attempt for retractable operation.")

                return block()
            } catch (e: Exception) {
                if (attempt == maxRetries - 1) {
                    Timber.e(e, "Max retries reached for operation.")
                    throw e
                }
                Timber.w(e, "Retrying operation after delay: $currentDelay ms")
                delay(currentDelay)
                currentDelay = (currentDelay * BACKOFF_MULTIPLIER).toLong()
                    .coerceAtMost(MAX_RETRY_DELAY)

            }
        }
        Timber.d("Final attempt for retractable operation.")
        return block() // Final attempt
    }

    suspend fun cancelAllOperations() {
        Timber.d("Cancelling all active operations.")
        mutex.withLock {
            activeOperations.values.forEach { job ->
                Timber.d("Cancelling operation job.")
                job.cancelAndJoin()
            }
            activeOperations.clear()
        }
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 3000L
        private const val DEFAULT_RETRIES = 3
        private const val INITIAL_DELAY = 1000L
        private const val MAX_RETRY_DELAY = 1000L
        private const val BACKOFF_MULTIPLIER = 2.0
    }
}