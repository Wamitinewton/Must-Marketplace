package com.newton.mustmarket.features.products.presentation.view.productList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.newton.mustmarket.core.sharedComposable.ErrorState
import com.newton.mustmarket.core.sharedComposable.LoadingState
import com.newton.mustmarket.core.sharedComposable.NoSearchResultsState
import com.newton.mustmarket.core.sharedComposable.SearchBar
import com.newton.mustmarket.features.products.presentation.event.SearchProductEvent
import com.newton.mustmarket.features.products.presentation.viewmodels.SearchProductsViewModel
import com.newton.mustmarket.navigation.Screen

@Composable
fun ProductSearchScreen(
    navController: NavController,
    searchViewModel: SearchProductsViewModel = hiltViewModel()
) {
    val searchUiState by searchViewModel.searchUiState.collectAsState()
    Scaffold(
        topBar = {
            SearchBar(
                autoFocus = true,
                onQueryChange = { searchEvent ->
                    searchViewModel.onSearchEvent(SearchProductEvent.QueryChanged(query = searchEvent))
                },
                onClearQuery = {
                    searchViewModel.onSearchEvent(SearchProductEvent.ClearSearch)
                },
                query = searchUiState.searchQuery,
                onSearch = {
                    searchViewModel.onSearchEvent(SearchProductEvent.PerformSearch)
                },
                isSearchActive = searchUiState.isSearchActive,
                onSearchNavigationClick = {
                    navController.navigateUp()
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = 20.dp, top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (searchUiState.isLoading) {
                item {
                    LoadingState()
                }
            } else {
                if (searchUiState.errorMessage?.isNotEmpty() == true) {
                    item {
                        ErrorState(
                            message = searchUiState.errorMessage ?: "An unexpected error occurred"
                        )
                    }
                } else if (searchUiState.searchQuery.isNotEmpty()) {
                    if (searchUiState.searchResults.isEmpty()) {
                        item {
                            NoSearchResultsState(searchQuery = searchUiState.searchQuery)
                        }
                    } else {
                        items(searchUiState.searchResults.size) { index ->
                            val product = searchUiState.searchResults[index]
                            ProductCard(
                                product = product,
                                onClick = {
                                    navController.navigate(Screen.Detail.createRoute(productId = product.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}