package com.newton.mustmarket.features.inbox.chatsList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.newton.mustmarket.navigation.Screen

@Composable
fun ExpandableFloatingActionButton(
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Additional action buttons (visible when expanded)
        if (expanded) {
            Column(
                modifier = Modifier.padding(bottom = 120.dp),
                horizontalAlignment = Alignment.End
            ) {
                ActionButton(
                    icon = Icons.Default.PersonAdd,
                    label = "Invite a Friend",
                    onClick = {
                        navController.navigate(Screen.InviteFriends.route)
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))
                ActionButton(
                    icon = Icons.Default.Archive,
                    label = "Archived Messages",
                    onClick = {
                        //navController.navigate("archived_messages")
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))
                ActionButton(
                    icon = Icons.Default.Calculate,
                    label = "Calculate",
                    onClick = {
                        //navController.navigate("calculator")
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))

                ActionButton(
                    icon = Icons.Default.Note,
                    label = "Notes",
                    onClick = {
                        //navController.navigate("notes")
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                ActionButton(
                    icon = Icons.Default.Star,
                    label = "Starred Messages",
                    onClick = {
                        //navController.navigate("starred_messages")
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                ActionButton(
                    icon = Icons.Default.Chat,
                    label = "New Chat",
                    onClick = {
                        navController.navigate(
                            Screen.NewChat.route
                        )
                    }
                )
                //check spacing
            }
        }

        // Main Floating Action Button
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 50.dp),
            onClick = { expanded = !expanded },
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Clear else Icons.Default.Add,
                contentDescription = if (expanded) "Close Menu" else "Open Menu",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
            )
        }
    }
}
