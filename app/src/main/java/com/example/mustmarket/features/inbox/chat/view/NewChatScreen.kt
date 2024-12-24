package com.example.mustmarket.features.inbox.chat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mustmarket.features.inbox.chat.domain.Contact
import com.example.mustmarket.features.inbox.chat.domain.RequestContactsPermission
import com.example.mustmarket.features.inbox.chat.domain.fetchContacts
import com.example.mustmarket.features.inbox.chatsList.model.Chat
import com.example.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import com.example.mustmarket.navigation.Screen

@Composable
fun NewChatScreen(
    navController: NavController,
    chatListViewModel: ChatListViewModel = viewModel()
) {
    val context = LocalContext.current
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }

    RequestContactsPermission {
        contacts = fetchContacts(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Start a New Chat",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            itemsIndexed(contacts) { index, contact ->
                Text(
                    text = "${contact.name} (${contact.phoneNumber})",
                    modifier = Modifier
                        .clickable {
                            val newChat = Chat(
                                id = contact.id,
                                contactName = contact.name,
                                lastMessage = "New chat started",
                                lastMessageTime = getCurrentTimestamp()
                            )
                            chatListViewModel.startNewChat(recipientName = contact.name)
                            // Create a new chat and navigate to the chat screen
                            navController.navigate(Screen.ChatScreen.createRoute(contact.phoneNumber))
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}

fun getCurrentTimestamp(): String {
    return java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
}
