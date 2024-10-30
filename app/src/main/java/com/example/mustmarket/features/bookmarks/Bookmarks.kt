package com.example.mustmarket.features.bookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mustmarket.features.home.presentation.state.BookmarksUiState
import com.example.mustmarket.features.home.presentation.viewmodels.BookmarksViewModel

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Bookmarks",
                    modifier = Modifier,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 22.sp
                    )
                )
            }
        },
        backgroundColor = MaterialTheme.colors.surface
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 55.dp, bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                                    onProductClick(product.id)
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