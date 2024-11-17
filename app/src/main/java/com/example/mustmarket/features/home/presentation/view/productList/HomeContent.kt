package com.example.mustmarket.features.home.presentation.view.productList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mustmarket.core.SharedComposables.ErrorState
import com.example.mustmarket.core.SharedComposables.LoadingAnimationType
import com.example.mustmarket.core.SharedComposables.LoadingState
import com.example.mustmarket.core.SharedComposables.NoSearchResultsState
import com.example.mustmarket.core.SharedComposables.SearchBar
import com.example.mustmarket.features.home.presentation.state.AllProductsViewModelState
import com.example.mustmarket.features.home.presentation.state.ProductCategoryViewModelState

@OptIn(ExperimentalFoundationApi::class)
@Composable
 fun HomeContent(
    uiState: AllProductsViewModelState,
    categoryUIState: ProductCategoryViewModelState,
    isLoading: Boolean,
    hasError: Boolean,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    onProductClick: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp, top = 40.dp)
    ) {
        // Search Bar
        item {
            SearchBar(
                autoFocus = false,
                onSearch = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        onSearch(uiState.searchQuery)
                    }
                },
                query = uiState.searchQuery,
                onQueryChange = { searchEvent -> onSearch(searchEvent.query) },
                onClearQuery = onClearSearch,
                isSearchActive = uiState.isSearchActive
            )
        }

        if (!uiState.isSearchActive && uiState.searchQuery.isEmpty()) {
            stickyHeader {
                HeaderBar()
            }

            item {
                AnimatedVisibility(
                    visible = !isLoading && !hasError,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Promotions()
                }
            }

            item {
                AnimatedVisibility(
                    visible = !isLoading && !hasError,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    CategoryGridView(

                    )
                }
            }

            item {
                ProductsHeader()
            }
        }

        // Content based on state
        when {
            isLoading -> {
                item {
                    LoadingState(
                        type = LoadingAnimationType.PULSING_DOTS,
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
            }

            hasError -> {
                item {
                    ErrorState(

                    )
                }
            }

            uiState.products.isEmpty() && uiState.searchQuery.isNotEmpty() -> {
                item {
                    NoSearchResultsState(
                        searchQuery = uiState.searchQuery,
                    )
                }
            }

            else -> {
                items(
                    count = uiState.products.size,
                    key = { index -> uiState.products[index].id }
                ) { index ->
                    val product = uiState.products[index]
                    ProductListItem(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )

                    if (index < uiState.products.size - 1) {
                        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                    }
                }
            }
        }
    }
}