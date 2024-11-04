package com.example.mustmarket.features.home.presentation.view.productList

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.ErrorState
import com.example.mustmarket.features.home.presentation.viewmodels.ProductCategoryViewModel
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import com.example.mustmarket.core.SharedComposables.LoadingState
import com.example.mustmarket.features.home.domain.model.ProductCategory
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.skydoves.landscapist.coil.CoilImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryGridView(
    viewModel: ProductCategoryViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,

        ) {
        CategoryHeader()
        Spacer(modifier = Modifier.height(8.dp))

        when {
            uiState.isLoading -> LoadingState()
            uiState.errorMessage.isNotEmpty() -> ErrorState()
            else -> CategoryGrid(categories = uiState.categories)
        }
    }
}

@Composable
private fun CategoryHeader() {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "All Categories",
                color = ThemeUtils.AppColors.Text.themed(),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun CategoryGrid(categories: List<ProductCategory>) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemMinSize = 90.dp
    val columns = maxOf(4, (screenWidth / itemMinSize).toInt())
    val itemsCount = categories.size
    val rows = (itemsCount + columns - 1) / columns
    val itemHeight = (screenWidth / columns)
    val spacing = 10.dp
    val totalHeight = (rows * itemHeight) + ((rows - 1) * spacing)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(categories.size) { index ->
                CategoryItem(category = categories[index], height = itemMinSize)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: ProductCategory,
    height: Dp
) {
    Card(
        onClick = {},
        modifier = modifier
            .aspectRatio(1f)
            .height(height)
            .padding(4.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CoilImage(
                    imageModel = {},
                    component = rememberImageComponent {
                        +ShimmerPlugin(
                            shimmer = Shimmer.Flash(
                                baseColor = Color.Gray,
                                highlightColor = Color.White,
                                duration = 500,
                                dropOff = 0.65F,
                                tilt = 20F
                            ),
                        )
                    },
                    failure = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_image),
                                contentDescription = "no image",
                                contentScale = ContentScale.Crop
                            )
                        }
                    },
                    previewPlaceholder = painterResource(id = R.drawable.ic_chinese_plum_flower),
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = category.name,
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                style = MaterialTheme.typography.caption.copy(
                    color = ThemeUtils.AppColors.SecondaryText.themed(),
                    fontSize = 12.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}