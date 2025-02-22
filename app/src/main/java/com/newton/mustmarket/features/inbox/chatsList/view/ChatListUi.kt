package com.newton.mustmarket.features.inbox.chatsList.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.newton.mustmarket.R
import com.newton.mustmarket.features.inbox.chatsList.presentation.ChatListItem
import com.newton.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import com.newton.mustmarket.navigation.Screen
import com.newton.mustmarket.ui.theme.gray01
import com.newton.mustmarket.ui.theme.greenishA

@Composable
fun ChatListScreen(
    navController: NavController,
    chatListViewModel: ChatListViewModel = hiltViewModel(),
) {

    val activeChats by chatListViewModel.activeChats.observeAsState(emptyList())

    Scaffold(
        backgroundColor = greenishA,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Market-Chat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = gray01
                        )
                    }
                    IconButton(
                        onClick = {
                            //
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.dslr_camera),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(gray01),
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Transparent)
                        )
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = gray01
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 50.dp),
                onClick = {
                    navController.navigate(Screen.NewChat.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Start New Chat"
                )
            }
        }
    ) { padding ->

        LazyColumn (
            modifier = Modifier
                .padding(padding)
        ){
            items(activeChats) { chat ->
                ChatListItem(
                    modifier = Modifier
                        .padding(16.dp),
                    chat = chat,
                    navController = navController
                )
            }
        }
    }
}

