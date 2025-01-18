package com.example.mustmarket.features.inbox.chat.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RequestContactsPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    var hasPermission by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        when (context.checkSelfPermission(Manifest.permission.READ_CONTACTS)) {
            PackageManager.PERMISSION_GRANTED -> {
                hasPermission = true
                onPermissionGranted()
            }
            else -> launcher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    if (!hasPermission) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Permission to access contacts is required.",
                style = MaterialTheme.typography.body2,
                color = Color.Red
            )
            Text(
                text = "Please grant contacts permission to continue.",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

suspend fun fetchContacts(
    context: Context,
    chatListViewModel: ChatListViewModel
): List<Contact> = withContext(Dispatchers.IO) {
    val contacts = mutableListOf<Contact>()

    try {
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        cursor?.use { c ->
            val nameIndex = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (c.moveToNext()) {
                val name = c.getString(nameIndex) ?: continue
                val phoneNumber = c.getString(numberIndex)?.replace("\\s".toRegex(), "") ?: continue

                // Check if contact with this number already exists to avoid duplicates
                if (contacts.none { it.phoneNumber == phoneNumber }) {
                    contacts.add(
                        Contact(
                            name = name,
                            phoneNumber = phoneNumber,
                            id = chatListViewModel.generateChatId()
                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle the error appropriately in your app
    }

    contacts
}