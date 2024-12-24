package com.example.mustmarket.features.inbox.chatsList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mustmarket.features.inbox.chat.domain.ChatMessage
import com.example.mustmarket.features.inbox.chatsList.model.Chat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatListViewModel : ViewModel() {
    private val _activeChats = MutableLiveData<List<Chat>>(emptyList())
    val activeChats: LiveData<List<Chat>> get() = _activeChats

    companion object {
        // This is a temporary solution. In a real app, you'd use dependency injection
        // or a proper communication pattern
        fun updateChat(chatId: String, lastMessage: String, timestamp: String) {
            instance._activeChats.value = instance._activeChats.value.orEmpty().map { chat ->
                if (chat.id == chatId) {
                    chat.copy(
                        lastMessage = lastMessage,
                        lastMessageTime = timestamp
                    )
                } else chat
            }
        }
        private lateinit var instance: ChatListViewModel
    }

    init {
        instance = this
        loadInitialChats()
    }

    fun startNewChat(recipientName: String): String {
        val chatId = generateChatId()
        val newChat = Chat(
            id = chatId,
            contactName = recipientName,
            lastMessage = "Start of conversation",
            lastMessageTime = getCurrentTimestamp()
        )
        _activeChats.value = _activeChats.value.orEmpty() + newChat
        return chatId
    }

    private fun loadInitialChats() {
        // In a real app, load from database or backend
        _activeChats.value = listOf(
            Chat(
                id = "chat1",
                contactName = "hh",
                lastMessage = "Hello! How can I help you?",
                lastMessageTime = "10:02 AM"
            )
        )
    }

    private fun generateChatId(): String {
        return "chat_${System.currentTimeMillis()}"
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

//    fun startChat(newChat: Chat) {
//        val currentChats = _activeChats.value.orEmpty()
//        _activeChats.value = currentChats + newChat
//    }
//
//
//    fun updateChat(chatId: String, lastMessage: String) {
//        _activeChats.value = _activeChats.value.orEmpty().map { chat ->
//            if (chat.id == chatId) {
//                chat.copy(
//                    lastMessage = lastMessage,
//                    lastMessageTime = getCurrentTimestamp()
//                )
//            } else chat
//        }
//    }
//
//    private fun getCurrentTimestamp(): String {
//        val dateFormat = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
//        return dateFormat.format(Date())
//    }

}

