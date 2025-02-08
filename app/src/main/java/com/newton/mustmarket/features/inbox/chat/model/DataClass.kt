package com.newton.mustmarket.features.inbox.chat.model

data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: String,
    val isSentByCurrentUser: Boolean,
    val chatId: String = ""
)

data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String
)