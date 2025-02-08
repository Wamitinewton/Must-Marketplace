package com.newton.mustmarket.core.util

import java.io.IOException

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

sealed class CustomError(
    override val message: String,
): IOException(message) {
    class NoConnectivityException(message: String) : CustomError(message)
    class NetworkTimeoutException(message: String) : CustomError(message)
    class BadRequestException(message: String) : CustomError(message)
    class UnauthorizedException(message: String) : CustomError(message)
    class ForbiddenException(message: String) : CustomError(message)
    class NotFoundException(message: String) : CustomError(message)
    class MethodNotAllowedException(message: String) : CustomError(message)
    class RequestTimeoutException(message: String) : CustomError(message)
    class ServerErrorException(message: String) : CustomError(message)
    class UnknownHttpException(message: String) : CustomError(message)
    class NetworkException(message: String) : CustomError(message)
}