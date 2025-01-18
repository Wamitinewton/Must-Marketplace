package com.example.mustmarket.features.inbox.chat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch

@Composable
fun NewChatScreen(
    navController: NavController,
    chatListViewModel: ChatListViewModel = viewModel(),
    currentUser: String = "anonymous"
) {
    val context = LocalContext.current
    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    RequestContactsPermission(
        onPermissionGranted = {
            coroutineScope.launch {
                isLoading = true
                try {
                    contacts = fetchContacts(context, chatListViewModel)
                } catch (e: Exception) {
                    error = "Failed to load contacts: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        },
        onPermissionDenied = {
            error = "Permission to access contacts is required"
        }
    )

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

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            contacts.isEmpty() -> {
                Text(
                    text = "No contacts found",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn {
                    itemsIndexed(contacts) { _, contact ->
                        ContactItem(
                            contact = contact,
                            onClick = {
                                val newChat = Chat(
                                    id = contact.id,
                                    contactName = contact.name,
                                    lastMessage = "New chat started",
                                    lastMessageTime = getCurrentTimestamp()
                                )
                                chatListViewModel.startNewChat(recipientName = contact.name)
                                navController.navigate(
                                    Screen.ChatScreen.route.replace(
                                        "{chatId}", contact.id
                                    ).replace(
                                        "{contactName}", contact.name
                                    ).replace(
                                        "{currentUser}", currentUser
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = contact.name,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = contact.phoneNumber,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private fun getCurrentTimestamp(): String {
    return java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
}