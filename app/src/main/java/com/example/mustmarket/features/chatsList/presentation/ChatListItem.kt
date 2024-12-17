package com.example.mustmarket.features.chatsList.presentation

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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.features.chatsList.model.Chat
import com.example.mustmarket.navigation.Screen

@Composable
fun ChatListItem(
    chat: Chat,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .clickable {
                //navController.navigate(Screen.ChatScreen.createRoute(chat.id))
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
            if (chat.profileImageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(chat.profileImageUrl),
                    contentDescription = "Profile Image",
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
                        text = chat.username?.firstOrNull()?.toString()?.uppercase() ?: "U",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Username and last message
            Column {
                Text(
                    text = chat.username ?: "Unknown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Row {
                    Icon(
                        imageVector = if (chat.isMessageSent) Icons.Default.Check else Icons.Default.AccessTime,
                        contentDescription = "Message Status",
                        tint = if (chat.isMessageSent) Color.Green else Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = chat.lastMessage,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Date and unread badge
        Column(horizontalAlignment = Alignment.End) {
            Text(text = chat.date, color = Color.Gray, fontSize = 12.sp)
            if (chat.unreadCount > 0) {
                Badge(
                    backgroundColor = Color.Red,
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
    Spacer(Modifier.height(4.dp))
}