package com.example.mustmarket.core.util

fun parsedErrorMessage(message: String): String {
    return when {
        message.contains("Conflict", ignoreCase = true) -> {
            "Email is already taken"
        }

        message.contains("Couldn't reach") -> {
            "Network error"
        }

        else -> {
            "Something went wrong"
        }
    }
}