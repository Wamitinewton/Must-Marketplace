package com.newton.mustmarket.core.sharedComposable

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NetworkAlertDialog(
    onDismiss: () -> Unit,
    onExit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Network Error",
                style = MaterialTheme.typography.h2.copy(
                    color = MaterialTheme.colors.error
                )
            )
        },
        text = {
            Text(
                text = "Please check your internet connection and try again. The app requires active network connection",
                style = MaterialTheme.typography.body1
            )
        },
        confirmButton = {
            Button(
                onClick = onExit,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.error
                )
            ) {
                Text(
                    text = "Exit App",
                    color = MaterialTheme.colors.onError
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Stay",
                    color = MaterialTheme.colors.primary
                )
            }
        }
    )
}