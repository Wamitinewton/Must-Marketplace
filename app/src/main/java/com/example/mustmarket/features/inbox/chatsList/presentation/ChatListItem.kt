package com.example.mustmarket.features.inbox.chatsList.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.R
import com.example.mustmarket.features.inbox.chatsList.model.Chat
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.gray01

@Composable
fun ChatListItem(
    chat: Chat,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .background(Color.DarkGray)
            .clickable {
                navController.navigate(Screen.ChatScreen.createRoute(chat.id))
            }
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image or initials
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                if (!chat.profileImageUrl.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = chat.profileImageUrl,
                            error = rememberVectorPainter(Icons.Default.BrokenImage),
                        ),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(getRandomBackgroundColor(chat.id)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.username?.firstOrNull()?.toString()?.uppercase() ?: "?",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Username and last message
            Column {
                Text(
                    text = chat.contactName ?: "Unknown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = if (chat.isMessageSent) Icons.Default.Check else Icons.Default.AccessTime,
                        contentDescription = "Message Status",
                        tint = if (chat.isMessageSent) MaterialTheme.colors.primarySurface else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = chat.lastMessage,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Date and unread badge
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chat.date,
                color = if (chat.unreadCount >= 1) MaterialTheme.colors.primarySurface else gray01,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (chat.unreadCount > 0) {
                Badge(
                    backgroundColor = MaterialTheme.colors.primarySurface,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}