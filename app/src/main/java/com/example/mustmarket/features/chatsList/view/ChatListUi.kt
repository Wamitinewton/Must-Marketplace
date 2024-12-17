package com.example.mustmarket.features.chatsList.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.mustmarket.R
import com.example.mustmarket.features.chatsList.presentation.ChatListItem
import com.example.mustmarket.features.chatsList.viewModel.ChatListViewModel

@Composable
fun ChatListScreen(viewModel: ChatListViewModel) {
    val chats by viewModel.chats.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Market-Chat", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* Search functionality */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { /* Camera functionality */ }) {
                    Icon(Icons.Default.Camera, contentDescription = "Camera")
                }
                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }
        )

        LazyColumn {
            items(chats) { chat ->
                ChatListItem(chat = chat)
            }
        }
    }
}

