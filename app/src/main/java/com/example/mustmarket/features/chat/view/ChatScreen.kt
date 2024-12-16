package com.example.mustmarket.features.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mustmarket.features.chat.domain.ChatMessage
import com.example.mustmarket.features.chat.presentation.ChatMessageItem
import com.example.mustmarket.features.chat.presentation.MessageInput
import com.example.mustmarket.features.chat.viewModel.ChatViewModel

@Composable
fun ChatScreen(
    userName: String = "",
    navController: NavController,
    viewModel: ChatViewModel = viewModel(),
) {
    val messages by viewModel.messages.collectAsState()
    var showMenuForMessage by remember { mutableStateOf<ChatMessage?>(null) }
    //var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            )
            .padding(bottom = 59.dp),
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = userName.firstOrNull()?.uppercase() ?: "unknown",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                    },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White
        )


        // Chat List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
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
            onMessageSent = {
                viewModel.sendMessage(it)
            }
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
