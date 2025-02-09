/////////////////////////////////////////////////////////////////////////////
//// no longer needed
/////////////////////////////////////////////////////////////////////////////
//package com.newton.mustmarket.features.inbox.chat.view
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material.CircularProgressIndicator
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Text
//import androidx.compose.material.TopAppBar
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.primarySurface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//<<<<<<<< HEAD:app/src/main/java/com/newton/mustmarket/features/inbox/chat/view/InviteFriendsScreen.kt
//import com.example.mustmarket.features.inbox.chat.model.Contact
//import com.example.mustmarket.features.inbox.chat.model.RequestContactsPermission
//import com.example.mustmarket.features.inbox.chat.model.fetchContacts
//import com.example.mustmarket.features.inbox.chat.presentation.ContactItem
//import com.example.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
//import com.example.mustmarket.ui.theme.ThemeUtils
//import com.example.mustmarket.ui.theme.ThemeUtils.themed
//import com.example.mustmarket.ui.theme.greenishA
//========
//import com.newton.mustmarket.features.inbox.chat.model.Contact
//import com.newton.mustmarket.features.inbox.chat.model.RequestContactsPermission
//import com.newton.mustmarket.features.inbox.chat.model.fetchContacts
//import com.newton.mustmarket.features.inbox.chat.presentation.ContactItem
//import com.newton.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
//import com.newton.mustmarket.navigation.Screen
//import com.newton.mustmarket.ui.theme.gray01
//import com.newton.mustmarket.ui.theme.greenishA
//>>>>>>>> main:app/src/main/java/com/newton/mustmarket/features/inbox/chat/view/NewChatScreen.kt
//import kotlinx.coroutines.launch
//
//@Composable
//fun InviteFriendsScreen(
//    navController: NavController,
//    chatListViewModel: ChatListViewModel = hiltViewModel(),
//) {
//    val context = LocalContext.current
//    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
//    var isLoading by remember { mutableStateOf(false) }
//    var error by remember { mutableStateOf<String?>(null) }
//    val coroutineScope = rememberCoroutineScope()
//
//    RequestContactsPermission(
//        onPermissionGranted = {
//            coroutineScope.launch {
//                isLoading = true
//                try {
//                    contacts = fetchContacts(context, chatListViewModel)
//                } catch (e: Exception) {
//                    error = "Failed to load contacts: ${e.message}"
//                } finally {
//                    isLoading = false
//                }
//            }
//        },
//        onPermissionDenied = {
//            error = "Permission to access contacts is required"
//        }
//    )
//
//    Scaffold(
//        backgroundColor = MaterialTheme.colors.primarySurface,
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "Select Contact",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 24.sp,
//                        color = ThemeUtils.AppColors.Text.themed()
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(
//                            Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back",
//                            tint = MaterialTheme.colors.onPrimary
//                        )
//                    }
//                },
//                backgroundColor = MaterialTheme.colors.primarySurface,
//                elevation = 0.dp
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(Color.White)
//        ) {
//            when {
//                isLoading -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator(color = greenishA)
//                    }
//                }
//                error != null -> {
//                    Text(
//                        text = error ?: "",
//                        color = MaterialTheme.colors.error,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//                contacts.isEmpty() -> {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "No contacts found",
//                            style = MaterialTheme.typography.body1,
//                            color = Color.Gray
//                        )
//                    }
//                }
//                else -> {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize()
//                    ) {
//                        itemsIndexed(contacts) {_, contact ->
//                            ContactItem(
//                                contact = contact,
//                                onClick = {
//                                    inviteViaWhatsApp(
//                                        context,
//                                        contact
//                                    )
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//private fun getCurrentTimestamp(): String {
//    return java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
//}
//
/////////////////////////////////////////////////////////////////////////////
//// whatsapp intention fun
/////////////////////////////////////////////////////////////////////////////
//
//private fun inviteViaWhatsApp(
//    context: Context,
//    contact: Contact
//){
//
//    val inviteMessage = """"Hey ${contact.name},
//         I just found an amazing app called **MustMarket** that makes buying, selling, and chatting super easy! 🛍️💬
//
//         Join me on Must now and let's connect. Trust me, you’ll love it! 🔥🔥
//
//         Download it here: [Play Store Link] """.trimIndent()
//
//    val formattedPhone = formatPhoneNumber(contact.phoneNumber)
//
//    val intent = Intent(Intent.ACTION_VIEW).apply {
//        data = Uri.parse("https://wa.me/${formattedPhone}/?text=${Uri.encode(inviteMessage)}")
//    }
//
//    // Check if WhatsApp is installed
//    if (intent.resolveActivity(context.packageManager) != null) {
//        context.startActivity(intent)
//    } else {
//        Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_LONG).show()
//    }
//}
//
/////////////////////////////////////////////////////////////////////////////
//// phone number formatting for whatsapp intent call
/////////////////////////////////////////////////////////////////////////////
//
//private fun formatPhoneNumber(rawNumber: String, defaultCountryCode: String = "+254"): String {
//    val trimmedNumber = rawNumber.trim().replace("\\s".toRegex(), "")
//
//    return when {
//        trimmedNumber.startsWith("+") -> trimmedNumber
//        trimmedNumber.startsWith("0") -> defaultCountryCode + trimmedNumber.drop(1) // Remove '0' and add country code
//        else -> defaultCountryCode + trimmedNumber
//    }
//}
