package com.example.mustmarket.features.inbox.chat.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mustmarket.features.inbox.chat.model.ChatMessage
import com.example.mustmarket.features.inbox.chat.model.ChatMessageEntity
import com.example.mustmarket.features.inbox.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {
    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> get() = _messages

    private val _currentChatId = MutableStateFlow<String?>(null)
    private var currentChatId: String? = null

    init {
        viewModelScope.launch {
            _currentChatId
                .filterNotNull()
                .flatMapLatest { chatId ->
                    repository.getChatMessages(chatId)
                }
                .collect { messageEntities ->
                    _messages.postValue(messageEntities.map { entity ->
                        ChatMessage(
                            sender = entity.sender,
                            message = entity.message,
                            timestamp = formatTimestamp(entity.timestamp),
                            entity.sender == "You", // Adjust based on your sender logic
                            chatId = entity.chatId
                        )
                    })
                }
        }
    }

    fun setCurrentChatId(chatId: String) {
        currentChatId = chatId
        _currentChatId.value = chatId
    }

    fun sendMessage(sender: String, message: String) {
        currentChatId?.let { chatId ->
            val timestamp = getCurrentTimestamp()
            val newMessage = ChatMessage(
                sender = sender,
                message = message,
                timestamp = timestamp,
                true,
                chatId = chatId
            )
            _messages.value = _messages.value.orEmpty() + newMessage

            viewModelScope.launch {
                repository.insertMessage(
                    ChatMessageEntity(
                        chatId = chatId,
                        sender = sender,
                        message = message,
                        timestamp = System.currentTimeMillis()
                    )
                )
                repository.updateChatLastMessage(chatId, message, timestamp)
            }
        }
    }

    fun receiveMessage(sender: String, message: String) {
        currentChatId?.let { chatId ->
            val timestamp = getCurrentTimestamp()
            val newMessage = ChatMessage(
                sender = sender,
                message = message,
                timestamp = timestamp,
                false,
                chatId = chatId
            )
            _messages.value = _messages.value.orEmpty() + newMessage
        }
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
}