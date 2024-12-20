package com.example.mustmarket.features.chatsList.model

data class Chat(
    val id: String,
    val username: String?,
    val profileImageUrl: String? = null,
    val lastMessage: String,
    val isMessageSent: Boolean = false,
    val date: String,
    val unreadCount: Int
)
