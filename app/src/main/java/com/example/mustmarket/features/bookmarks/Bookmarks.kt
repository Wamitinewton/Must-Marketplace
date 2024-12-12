package com.example.mustmarket.features.bookmarks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mustmarket.core.SharedComposables.SearchBar
import com.example.mustmarket.features.home.presentation.state.BookmarksUiState
import com.example.mustmarket.features.home.presentation.viewmodels.BookmarksViewModel
import com.example.mustmarket.navigation.Screen

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showNumberIndicator by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "My Bookmarks",
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.White
            )
        }
        SearchBar(
            autoFocus = false,
            onQueryChange = {},
            onClearQuery = {},
            query = "",
            onSearch = {},
            isSearchActive = false,
            onSearchNavigationClick = {}
        )

        AnimatedVisibility(visible = showNumberIndicator) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(
                        color = MaterialTheme.colors.primary.copy(alpha = 0.25F),
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = when (uiState) {
                            is BookmarksUiState.Success -> {
                                val bookmarks = (uiState as BookmarksUiState.Success).bookmarks
                                val count = bookmarks?.size ?: 0
                                if (count == 1) "1 Bookmarked Product"
                                else "$count Bookmarked Products"
                            }

                            is BookmarksUiState.Loading -> "Loading bookmarks..."
                            is BookmarksUiState.Error -> "No bookmarks"
                        },
                        color = Color.White
                    )
                    IconButton(
                        onClick = { showNumberIndicator = false }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Hide Counter",
                            tint = Color.White
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (uiState) {
            is BookmarksUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is BookmarksUiState.Success -> {
                val bookmarks = (uiState as BookmarksUiState.Success).bookmarks
                if (bookmarks.isNullOrEmpty()) {
                    EmptyBookmarksMessage()
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = bookmarks,
                            key = { it.id }
                        ) { product ->
                            SwipeToDelete(
                                product = product,
                                onDelete = {
                                    viewModel.removeBookmark(product)
                                },
                                onClick = {
                                    navController.navigate(Screen.Detail.createRoute(product.id))
                                }
                            )

                        }
                    }
                }
            }

            is BookmarksUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as BookmarksUiState.Error).message ?: "An Error occurred",
                        color = MaterialTheme.colors.error
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyBookmarksMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No bookmarks yet",
                style = MaterialTheme.typography.h2,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Items you bookmark will appear here",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}