package com.example.mustmarket.features.inbox.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mustmarket.features.inbox.chat.model.Contact
import com.example.mustmarket.features.inbox.chat.model.RequestContactsPermission
import com.example.mustmarket.features.inbox.chat.model.fetchContacts
import com.example.mustmarket.features.inbox.chat.presentation.ContactItem
import com.example.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.gray01
import com.example.mustmarket.ui.theme.greenishA
import kotlinx.coroutines.launch

@Composable
fun InviteFriendsScreen(
    navController: NavController,
    chatListViewModel: ChatListViewModel = hiltViewModel(),
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

    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Select Contact",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = ThemeUtils.AppColors.Text.themed()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primarySurface,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = greenishA)
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No contacts found",
                            style = MaterialTheme.typography.body1,
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(contacts) { _, contact ->
                            ContactItem(
                                contact = contact,
                                onClick = {
//                                    val newChat = Chat(
//                                        id = contact.id,
//                                        contactName = contact.name,
//                                        lastMessage = "New chat started",
//                                        lastMessageTime = getCurrentTimestamp()
//                                    )
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
}


private fun getCurrentTimestamp(): String {
    return java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
}