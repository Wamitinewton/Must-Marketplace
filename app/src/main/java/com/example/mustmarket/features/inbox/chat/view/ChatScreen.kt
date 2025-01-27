package com.example.mustmarket.features.inbox.chat.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mustmarket.features.inbox.chat.model.ChatMessage
import com.example.mustmarket.features.inbox.chat.presentation.ChatMessageItem
import com.example.mustmarket.features.inbox.chat.presentation.MessageInput
import com.example.mustmarket.features.inbox.chat.viewModel.ChatViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun ChatScreen(
    navController: NavController,
    chatId: String,
    contactName: String,
    currentUser: String,
    chatViewModel: ChatViewModel = hiltViewModel(),
    //chatListViewModel: ChatListViewModel = viewModel()
) {

    val messages by chatViewModel.messages.observeAsState(emptyList())
    var showMenuForMessage by remember { mutableStateOf<ChatMessage?>(null) }

    LaunchedEffect(chatId) {
        chatViewModel.setCurrentChatId(chatId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = contactName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = ThemeUtils.AppColors.Text.themed()
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.ChatListScreen.route){
                                popUpTo(Screen.ChatListScreen.route){
                                    inclusive = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "navigate back"
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
                .padding(bottom = 1.dp),
        ) {


            // scrollable chat
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                items(messages/*.reversed()*/) { message ->
                    ChatMessageItem(
                        message = message,
                        onMenuClicked = {
                            showMenuForMessage = it
                        },
                    )
                }
            }

            // Message Input
            MessageInput(
                    onMessageSent = { message ->
                        chatViewModel.sendMessage(
                            sender = currentUser,
                            message = message
                        )
                },
                currentUser = currentUser
            )

            showMenuForMessage?.let { message ->
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = { showMenuForMessage = null },

                    ) {
                    DropdownMenuItem(onClick = { }) {
                        Text("Edit")
                    }
                    DropdownMenuItem(onClick = { /* Delete Logic */ }) {
                        Text("Delete")
                    }
                    DropdownMenuItem(onClick = { /* Copy Logic */ }) {
                        Text("Copy")
                    }
                    DropdownMenuItem(onClick = { /* reply Logic */ }) {
                        Text("Reply")
                    }
                    DropdownMenuItem(onClick = { /* forward Logic */ }) {
                        Text("Forward")
                    }
                    DropdownMenuItem(onClick = { /* react Logic */ }) {
                        Text("React")
                    }
                }
            }
        }
    }
}

//fun simulateBackendMessages(chatViewModel: ChatViewModel) {
//    CoroutineScope(Dispatchers.IO).launch {
//        Thread.sleep(5000) // Simulate delay
//        chatViewModel.receiveMessage("Demola", "This is a message from the server!")
//    }
//}
