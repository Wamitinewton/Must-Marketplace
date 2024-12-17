package com.example.mustmarket.features.chatsList.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.mustmarket.features.chatsList.presentation.ChatListItem
import com.example.mustmarket.features.chatsList.viewModel.ChatListViewModel
import com.example.mustmarket.ui.theme.colorPrimary
import com.example.mustmarket.ui.theme.greenishA

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel
) {
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

        Column (
            modifier = Modifier
                .background(
                    color = greenishA
                )
                .fillMaxSize()
        ){
            LazyColumn {
                items(chats) { chat ->
                    ChatListItem(
                        chat = chat,
                        navController = navController
                    )
                }
            }
        }
    }
}

