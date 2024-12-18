package com.example.mustmarket.features.chatsList.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mustmarket.R
import com.example.mustmarket.features.chatsList.presentation.ChatListItem
import com.example.mustmarket.features.chatsList.viewModel.ChatListViewModel
import com.example.mustmarket.ui.theme.gray01
import com.example.mustmarket.ui.theme.greenishA

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel
) {
    val chats by viewModel.chats.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .background(
                color = greenishA
            )
            .fillMaxSize()
    ) {
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

