package com.example.mustmarket.features.chatsList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mustmarket.features.chatsList.model.Chat

class ChatListViewModel : ViewModel() {
    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    init {
        loadChats()
    }

    private fun loadChats() {
        // Simulated chat data
        val sampleChats = listOf(
            Chat("1", "John Doe", null, "Hey! How are you?", true, "10:45 AM", 2),
            Chat("2", null, null, "Let's meet tomorrow.", false, "Yesterday", 0),
            Chat("3", "Alice", "https://photos.app.goo.gl/G5j6peRZ538DKMPo8", "Sure! I'll bring it.", true, "Mon", 5),
            Chat("4", "Bob", null, "See you later.", true, "17/12/2024", 1)
        )
        _chats.value = sampleChats
    }
}
