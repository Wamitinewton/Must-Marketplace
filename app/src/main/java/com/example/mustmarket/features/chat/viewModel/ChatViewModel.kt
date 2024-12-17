package com.example.mustmarket.features.chat.viewModel

import androidx.lifecycle.ViewModel
import com.example.mustmarket.features.chat.domain.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage(
                "Demola",
                "Hi Abiola, any progress on the project? We need a link for standup. " +
                        "darktunnel://eyJ0eXBlIjoiU1NIIiwibmFtZSI6IkRlZm F1bHQiLCJzc2hUdW5uZWxDb25maWciOnsi" +
                        "c3NoQ29uZmlnIjp7Imhvc3QiOiJjYWN0dXMuYnlyb25qdXN0aW5lLnN0b3JlIiwicG9ydCI6ODAsInVzZXJuYW1" +
                        "lIjoiRGV4dGVyIiwicGFzc3dvcmQiOiJEZVgifSwiaW5qZWN0Q29uZmlnIjp7Im1vZGUiOiJQUk9YWSIsInByb3h5SG9z" +
                        "dCI6IjE0MS4xOTMuMjEzLjEwIiwicHJveHlQb3J0Ijo4MCwicGF5bG9hZCI6IkdFVCAvY2RuLWNnaS90cmFjZSBIVFRQ" +
                        "LzEuMVtjcmxmXUhvc3Q6IFtob3N0XVtjcmxmXVtjcmxmXUNGLVJBWSAvIEhUVFAvMS4xW2NybGZdSG9zdDogW2hvc3RdW2Ny" +
                        "bGZdVXBncmFkZTogV2Vic29ja2V0W2NybGZdQ29ubmVjdGlvbjogS2VlcC1BbGl2ZVtjcmxmXVVzZXItQWdlbnQ6IFt1YV1bY3" +
                        "JsZl1VcGdyYWRlOiB3ZWJzb2NrZXRbY3JsZl1bY3JsZl0ifX19",
                "1 day ago",
                false
            ),
            ChatMessage("Abiola", "Hi Demola! Yes, I just finished developing the 'Chat' template.", "1 day ago", true),
            ChatMessage("Demola", "Job Description.docx", "2 min ago", false),
            ChatMessage("Abiola", "Hello Demola, what job role is this document for?", "1 min ago", true),
            ChatMessage("Demola", "Technical Project Manager role. Hybrid, 3 days on site a week.", "1 min ago", false)
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(message: String) {
        val newMessage = ChatMessage("Abiola", message, "Just now", true)
        _messages.value += newMessage
    }
}