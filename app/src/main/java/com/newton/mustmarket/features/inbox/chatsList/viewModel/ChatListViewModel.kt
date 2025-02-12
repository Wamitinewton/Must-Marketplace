package com.newton.mustmarket.features.inbox.chatsList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.auth.domain.repository.AuthRepository
import com.newton.mustmarket.features.inbox.chatsList.model.Chat
import com.newton.mustmarket.features.inbox.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<AuthedUser>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Convert Flow to LiveData for active chats
    val activeChats = repository.getAllChats()
        .map { chatEntities ->
            chatEntities.map { entity ->
                Chat(
                    id = entity.chatId,
                    contactName = entity.contactName,
                    lastMessage = entity.lastMessage,
                    lastMessageTime = entity.lastMessageTime
                )
            }
        }.asLiveData()

    fun startNewChat(recipientName: String): String {
        val chatId = generateChatId()
        viewModelScope.launch {
            repository.createChat(
                chatId = chatId,
                contactName = recipientName,
                initialMessage = "Start of conversation",
                timestamp = getCurrentTimestamp()
            )
        }
        return chatId
    }

    fun updateChat(chatId: String, lastMessage: String) {
        viewModelScope.launch {
            repository.updateChatLastMessage(
                chatId = chatId,
                lastMessage = lastMessage,
                timestamp = getCurrentTimestamp()
            )
        }
    }

    fun generateChatId(): String {
        return "chat_${System.currentTimeMillis()}"
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
            .format(Date())
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val user = authRepository.getLoggedInUser() // Fetch authenticated user
                _users.value = user?.let { listOf(it) } ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load users: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}

