package com.example.mustmarket.features.inbox.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
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
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.greenishA

@Composable
fun MessageInput(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    currentUser: String
) {
    val chatViewModel: ChatViewModel = viewModel()
    var messageText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Surface(
        elevation = 8.dp,
        color = MaterialTheme.colors.surface
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp,)
                .background(
                    color = greenishA,
                    shape = RoundedCornerShape(24.dp)
                )
                .imePadding()
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    expanded = !expanded
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Attachment",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(24.dp),
                )
            }

            // Attachment Menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                    /* Open gallery */
                    }
                ) {
                    Icon(
                        Icons.Default.Image,
                        null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 4.dp)
                    )

                    Text(
                        "Photo",
                        color = ThemeUtils.AppColors.Text.themed()
                        )
                }

                DropdownMenuItem(
                    onClick = { /* Open camera */ }
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 4.dp)
                        )
                    Text(
                        "Camera",
                        color = ThemeUtils.AppColors.Text.themed()
                    )
                }
                DropdownMenuItem(
                    onClick = { /* Open document picker */ }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.InsertDriveFile,
                        null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 4.dp)
                    )
                    Text(

                        "Document",
                        color = ThemeUtils.AppColors.Text.themed()
                        )
                }
            }


            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                textStyle = TextStyle(
                    color = ThemeUtils.AppColors.Text.themed(),
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
                modifier = modifier
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
                modifier = Modifier,
                onClick = {
                    if (messageText.isNotBlank()) {
                        chatViewModel.sendMessage(currentUser, messageText)
                        //onMessageSent(message)
                        messageText = "" // Clear the input
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
}
