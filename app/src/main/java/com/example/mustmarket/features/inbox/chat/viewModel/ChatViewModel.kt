package com.example.mustmarket.features.inbox.chat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mustmarket.features.inbox.chat.domain.ChatMessage
import com.example.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> get() = _messages

    private var currentChatId: String? = null

    fun setCurrentChatId(chatId: String) {
        currentChatId = chatId
        loadInitialMessages(chatId)
    }
//    init {
//        loadInitialMessages()// Load initial messages (e.g., from backend or mock data)
//    }

    fun sendMessage(sender: String, message: String) {
        currentChatId?.let { chatId ->
            val timestamp = getCurrentTimestamp()
            val newMessage = ChatMessage(
                sender,
                message,
                timestamp,
                true,
                chatId = chatId
            )
            _messages.value = _messages.value.orEmpty() + newMessage

            updateChatList(chatId, message, timestamp)

        }
    }

    private fun updateChatList(chatId: String, lastMessage: String, timestamp: String) {
        // Notify ChatListViewModel about the update
        // You can use a shared ViewModel or other communication pattern
        // For example, using EventBus or a shared repository
        ChatListViewModel.updateChat(chatId, lastMessage, timestamp)
    }

    fun receiveMessage(sender: String, message: String) {
        val timestamp = getCurrentTimestamp()
        val newMessage = ChatMessage(sender, message, timestamp, false)
        //_messages.value = _messages.value.orEmpty() + newMessage
        CoroutineScope(Dispatchers.IO).launch {
            // Use postValue for background thread
            _messages.postValue(_messages.value.orEmpty() + newMessage)
        }
    }

    private fun loadInitialMessages( chatId: String) {
        /// In a real app, you would load messages from a database or backend
        // For now, we'll simulate loading messages
        _messages.value = listOf(
            ChatMessage("Demola", "Hi there!", "10:00 AM", false, chatId),
            ChatMessage("Abiola", "Hello! How can I help you?", "10:02 AM", true, chatId)
        )
    }


//    private fun syncWithBackend(message: ChatMessage) {
//        CoroutineScope(Dispatchers.IO).launch {
//            // Simulate sending the message to a backend
//            // Replace this with an actual API call
//            Thread.sleep(1000) // Simulate network delay
//        }
//    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

}