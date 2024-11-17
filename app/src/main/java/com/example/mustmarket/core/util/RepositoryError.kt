package com.example.mustmarket.core.util

sealed class RepositoryError(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    class Network(message: String, cause: Throwable? = null) :
        RepositoryError(message, cause)

    class Database(message: String, cause: Throwable? = null) :
        RepositoryError(message, cause)

    class Cache(message: String, cause: Throwable? = null) :
        RepositoryError(message, cause)

    class Api(message: String, cause: Throwable? = null) :
        RepositoryError(message, cause)
}