package com.newton.mustmarket.features.inbox.chatsList.model

data class Chat(
    val id: String,
    val username: String? = null, // Optional username
    val profileImageUrl: String? = null, // Optional profile image URL
    val lastMessage: String = "",
    val isMessageSent: Boolean = false,
    val date: String = "", // Optional date, defaults to empty string
    val unreadCount: Int = 0, // Optional unread count, defaults to 0
    val contactName: String? = "",
    val lastMessageTime: String = ""
)
