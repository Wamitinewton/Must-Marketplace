package com.example.mustmarket.features.inbox.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.mustmarket.features.inbox.chat.domain.ChatMessage
import com.example.mustmarket.ui.theme.Purple700
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.colorPrimary


@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onMenuClicked: (ChatMessage) -> Unit
) {
    val layoutDirection =
        if (message.isSentByCurrentUser) LayoutDirection.Ltr else LayoutDirection.Rtl
    val chatBubbleShape = ChatBubbleShape(
        isSentByCurrentUser = message.isSentByCurrentUser
    )

    // State for expanding/collapsing long messages
    var isExpanded by remember { mutableStateOf(false) }//readmore state
    val maxMessageLength = 200 // Max char limit before showing "read more

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = if (message.isSentByCurrentUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (message.isSentByCurrentUser) {
                HorizontalDotsIcon(
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable { onMenuClicked(message) }
                )
            }

                //bubble
            Column(
                modifier = Modifier
                    .background(
                        if (message.isSentByCurrentUser) ThemeUtils.AppColors.Primary else ThemeUtils.AppColors.ChatBubble.themed(),
                        shape = chatBubbleShape
                    )
                    .padding(15.dp)
                    .widthIn(max = 250.dp)
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    Text(
                        text = if (isExpanded || message.message.length <= maxMessageLength) {
                            message.message
                        } else {
                            "${message.message.take(maxMessageLength)}..."
                        },
                        color = ThemeUtils.AppColors.Text.themed(),
                        style = MaterialTheme.typography.body1,
                        textAlign = if (message.isSentByCurrentUser) TextAlign.Start else TextAlign.Start,
                        )

                    // "More" clickable for long messages
                    if (message.message.length > maxMessageLength && !isExpanded) {
                        Text(
                            text = "Read More",
                            color = ThemeUtils.AppColors.Primary,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clickable { isExpanded = true }
                        )
                    } else if (isExpanded) {
                        Text(
                            text = "Read Less",
                            color = ThemeUtils.AppColors.Primary,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clickable { isExpanded = false }
                        )
                    }

                    if (message.isSentByCurrentUser) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Sent Tick",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.End)
                                .size(16.dp)
                        )
                    }
                }
            }

            if (!message.isSentByCurrentUser) {
                HorizontalDotsIcon(
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onMenuClicked(message) }
                )
            }


        }

        Row(
            modifier = Modifier
                //.background(Color.Gray)
                .fillMaxWidth()
                //.widthIn(270.dp)
                .padding(top = 2.dp),
            horizontalArrangement = if (message.isSentByCurrentUser) Arrangement.End else Arrangement.Start
        ) {
            Text(// Timestamp
                text = message.timestamp,
                style = MaterialTheme.typography.caption,
                color = Color.LightGray,
                textAlign = if (message.isSentByCurrentUser) TextAlign.End else TextAlign.Start,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                //.fillMaxWidth()
                //.widthIn(270.dp)
            )
        }
    }
}




