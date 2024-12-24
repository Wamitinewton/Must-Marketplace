package com.example.mustmarket.features.inbox.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mustmarket.features.inbox.chat.viewModel.ChatViewModel
import com.example.mustmarket.ui.theme.greenishA

@Composable
fun MessageInput(
    onMessageSent: (String) -> Unit,
    currentUser: String
) {
    val chatViewModel: ChatViewModel = viewModel()
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, )
            .background(
                color = greenishA,
                shape = RoundedCornerShape(24.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
            /* Handle add files button click */
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp),
            )
        }
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = MaterialTheme.typography.body2.fontSize
            ),
            placeholder = {
                Text(
                    "Type a message..."
                )
                          },
            shape = RoundedCornerShape(24.dp),
            singleLine = false,
            maxLines = 5,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        IconButton(
            modifier = Modifier
                ,
            onClick = {
                if (message.isNotBlank()) {
                    chatViewModel.sendMessage(currentUser, message)
                    //onMessageSent(message)
                    message = "" // Clear the input
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        translationY = -5.dp.toPx() // Move icon up slightly
                        rotationZ = 315f
                    }
            )
        }
    }
}
