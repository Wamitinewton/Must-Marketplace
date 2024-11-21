package com.example.mustmarket.core.interceptor

import android.content.Context
import com.example.mustmarket.core.networkManager.NetworkConnectionState
import com.example.mustmarket.core.networkManager.currentConnectivityState
import com.example.mustmarket.core.util.CustomError
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (context.currentConnectivityState == NetworkConnectionState.Unavailable) {

            throw CustomError.NoConnectivityException("No internet connection available")

        }

        try {
            val response = chain.proceed(request)

            when (response.code) {

                in 200..299 -> return response

                400 -> throw CustomError.BadRequestException("Bad request: ${response.body?.string() ?: "Unknown error"}")

                401 -> throw CustomError.UnauthorizedException("Unauthorized: Authentication failed")

                403 -> throw CustomError.ForbiddenException("Forbidden: You don't have permission")

                404 -> throw CustomError.NotFoundException("Not Found: Requested resource doesn't exist")

                405 -> throw CustomError.MethodNotAllowedException("Method Not Allowed")

                408 -> throw CustomError.RequestTimeoutException("Request Timeout")

                in 500..599 -> throw CustomError.ServerErrorException("Server Error: ${response.body?.string() ?: "Unknown error"}")

                else -> throw CustomError.UnknownHttpException("Unknown HTTP Error: ${response.code}")

            }
        } catch (e: SocketTimeoutException) {

            throw CustomError.NetworkTimeoutException("Network Timeout: ${e.message}")

        } catch (e: UnknownHostException) {

            throw CustomError.NoConnectivityException("Unable to resolve host: ${e.message}")

        }
    }
}