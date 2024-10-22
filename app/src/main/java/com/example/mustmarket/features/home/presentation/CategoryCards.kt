package com.example.mustmarket.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mustmarket.R
import com.example.mustmarket.features.home.presentation.viewmodels.ProductCategoryViewModel
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryGridView(
    viewModel: ProductCategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val itemsCount = uiState.categories.size
    val columns = 4
    val itemHeight = 90.dp
    val spacing = 10.dp

    val rows = (itemsCount + columns - 1) / columns

    val totalHeight = (rows * itemHeight) + ((rows - 1) * spacing)

    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Product categories")
        }

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            }

            uiState.errorMessage.isNotEmpty() -> {
                Text(
                    text = "Server error. Try again later",
                    color = MaterialTheme.colors.error,
                    style = TextStyle(
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(totalHeight + 30.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        verticalArrangement = Arrangement.spacedBy(spacing),
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        items(itemsCount) { index ->
                            val category = uiState.categories[index]
                            Card(
                                onClick = {},
                                modifier = Modifier
                                    .padding(4.dp)
                                    .height(itemHeight)
                                    .width(itemHeight),
                                elevation = 6.dp,
                            ) {
                                Column(
                                    Modifier.padding(3.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    GlideImage(imageModel = { "" },
                                        modifier = Modifier.size(50.dp),
                                        component = rememberImageComponent {

                                            +ShimmerPlugin(
                                                Shimmer.Flash(
                                                    baseColor = Color.White,
                                                    highlightColor = Color.LightGray,
                                                ),
                                            )
                                        },

                                        failure = {
                                            Image(
                                                contentDescription = null,
                                                painter = painterResource(id = R.drawable.no_image),

                                                )
                                        })
                                    Spacer(modifier = Modifier.height(9.dp))
                                    Text(
                                        modifier = Modifier.padding(top = 3.dp),
                                        text = category.name,

                                        style = MaterialTheme.typography.caption.copy(
                                            color = Color.Gray
                                        ),
                                        maxLines = 1
                                    )
                                }


                            }
                        }
                    }
                }
            }
        }
    }
}