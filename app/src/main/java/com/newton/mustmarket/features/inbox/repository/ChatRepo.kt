package com.newton.mustmarket.features.inbox.repository

<<<<<<< HEAD:app/src/main/java/com/example/mustmarket/features/inbox/repository/ChatRepo.kt
import com.example.mustmarket.database.dao.UserDao
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.inbox.chat.model.ChatDao
import com.example.mustmarket.features.inbox.chat.model.ChatEntity
import com.example.mustmarket.features.inbox.chat.model.ChatMessageEntity
=======
import com.newton.mustmarket.features.inbox.chat.model.ChatDao
import com.newton.mustmarket.features.inbox.chat.model.ChatEntity
import com.newton.mustmarket.features.inbox.chat.model.ChatMessageEntity
>>>>>>> main:app/src/main/java/com/newton/mustmarket/features/inbox/repository/ChatRepo.kt
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val userDao: UserDao
) {
    fun getAllChats() = chatDao.getAllChats()

    suspend fun insertMessage(message: ChatMessageEntity) {
        chatDao.insertMessage(message)
    }

    fun getChatMessages(chatId: String): Flow<List<ChatMessageEntity>> {
        return chatDao.getChatMessages(chatId)
    }

    suspend fun saveMessage(message: ChatMessageEntity) {
        chatDao.insertMessage(message)
    }

    suspend fun createChat(
        chatId: String,
        contactName: String,
        initialMessage: String,
        timestamp: String
    ) {
        val chat = ChatEntity(
            chatId = chatId,
            contactName = contactName,
            lastMessage = initialMessage,
            lastMessageTime = timestamp
        )
        chatDao.insertChat(chat)
    }

    suspend fun updateChatLastMessage(
        chatId: String,
        lastMessage: String,
        timestamp: String
    ) {
        chatDao.updateChatLastMessage(chatId, lastMessage, timestamp)
    }

    suspend fun fetchUsers(): List<AuthedUser> {
        return userDao.getAllUsers()
    }

    //suspend fun getAllUsers() = userDao.getAllUsers()

}

