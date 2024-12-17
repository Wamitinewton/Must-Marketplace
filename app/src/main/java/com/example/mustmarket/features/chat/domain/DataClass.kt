package com.example.mustmarket.features.chat.domain

data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: String,
    val isSentByCurrentUser: Boolean

)