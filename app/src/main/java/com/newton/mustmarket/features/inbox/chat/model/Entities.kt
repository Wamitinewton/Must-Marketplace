package com.newton.mustmarket.features.inbox.chat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chatId: String,
    val sender: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

// Chat.kt - Entity
@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val contactName: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0
)