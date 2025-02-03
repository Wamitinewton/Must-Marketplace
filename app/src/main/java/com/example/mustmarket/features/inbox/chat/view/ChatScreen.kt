package com.example.mustmarket.features.inbox.chat.view

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    navController: NavController,
    chatId: String,
    contactName: String,
    currentUser: String,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val messages by chatViewModel.messages.observeAsState(emptyList())
    var showMenuForMessage by remember { mutableStateOf<ChatMessage?>(null) }
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    LaunchedEffect(chatId) {
        chatViewModel.setCurrentChatId(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                title = {
                    Text(
                        text = contactName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = ThemeUtils.AppColors.Text.themed()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Video call */ }) {
                        Icon(
                            Icons.Default.Videocam,
                            contentDescription = "Video call"
                        )
                    }
                    IconButton(onClick = { /* Voice call */ }) {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = "Voice call"
                        )
                    }
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primarySurface,
            )
        },
        bottomBar = {
            MessageInput(
                onMessageSent = { message ->
                    chatViewModel.sendMessage(
                        sender = currentUser,
                        message = message
                    )
                },
                currentUser = currentUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                reverseLayout = true,
                state = rememberLazyListState()
            ) {
                items(messages) { message ->
                    ChatMessageItem(
                        message = message,
                        onMenuClicked = { showMenuForMessage = it },
                    )
                }
            }

            // Dropdown menu
            showMenuForMessage?.let { message ->
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = { showMenuForMessage = null }
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


//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .navigationBarsPadding()
//        ) {
//            // TopAppBar outside of Scaffold to prevent it from being affected by IME
//            TopAppBar(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                title = {
//                    Text(
//                        text = contactName,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp,
//                        color = ThemeUtils.AppColors.Text.themed()
//                    )
//                },
//                navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            navController.navigate(Screen.ChatListScreen.route) {
//                                popUpTo(Screen.ChatListScreen.route) {
//                                    inclusive = false
//                                }
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBackIosNew,
//                            contentDescription = "navigate back"
//                        )
//                    }
//                },
//                backgroundColor = MaterialTheme.colors.primarySurface,
//                elevation = 0.dp
//            )
//
//
//            // Chat content
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxSize()
//                    .padding(horizontal = 16.dp),
//                reverseLayout = true,
//                state = rememberLazyListState() // This helps with smooth scrolling
//            ) {
//                items(messages) { message ->
//                    ChatMessageItem(
//                        message = message,
//                        onMenuClicked = {
//                            showMenuForMessage = it
//                        },
//                    )
//                }
//            }
//
//            // Message input field at the bottom
//            MessageInput(
//                onMessageSent = { message ->
//                    chatViewModel.sendMessage(
//                        sender = currentUser,
//                        message = message
//                    )
//                },
//                currentUser = currentUser,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .navigationBarsPadding()
//                    .imePadding() // This ensures the input stays above keyboard
//            )
//
//
//
//        }
//    }
//}



