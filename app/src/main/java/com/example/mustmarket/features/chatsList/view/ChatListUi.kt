package com.example.mustmarket.features.chatsList.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Camera
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
import com.example.mustmarket.R
import com.example.mustmarket.features.chatsList.presentation.ChatListItem
import com.example.mustmarket.features.chatsList.viewModel.ChatListViewModel
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.gray01
import com.example.mustmarket.ui.theme.greenishA

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val chats by viewModel.chats.observeAsState(emptyList())

    Scaffold(
        backgroundColor = ThemeUtils.AppColors.Surface.themed(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Market-Chat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = ThemeUtils.AppColors.Text.themed()
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
                    //navController.navigate(ChatRoutes.NewChat.route)
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
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(chats) { chat ->
                ChatListItem(
                    chat = chat,
                    navController = navController
                )
            }
        }
    }
}

