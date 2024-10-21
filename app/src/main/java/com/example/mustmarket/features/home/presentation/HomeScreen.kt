package com.example.mustmarket.features.home.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.AppBarPrimary
import com.example.mustmarket.core.SharedComposables.TopProduct
import com.example.mustmarket.features.home.presentation.viewmodels.ProductCategoryViewModel
import com.example.mustmarket.ui.theme.colorPrimary
import com.example.mustmarket.ui.theme.favourite

@Composable
fun HomeScreen(
    navController: NavController,
    productCategoryViewModel: ProductCategoryViewModel = hiltViewModel()
) {
    val uiState by productCategoryViewModel.uiState.collectAsState()

    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .offset(0.dp, (-30).dp),
            painter = painterResource(id = R.drawable.bg_main),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Content(
            uiState = uiState
        )
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    uiState: ProductCategoryViewModelState
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item { AppBarPrimary() }
        item { HeaderBar() }
        item { Promotions() }
        item { CategoryGridView() }
        item { TopProduct() }
        item { ProductCard() }
    }
}


@Composable
fun HeaderBar(modifier: Modifier = Modifier) {
    Card(
        Modifier
            .height(64.dp)
            .padding(horizontal = 16.dp)
            ,
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QrButton()
            VerticalDivider()
            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {}
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    tint = Color(0xFF6FCF97)
                )
                Column(Modifier.padding(8.dp)) {
                    Text(
                        text = "Account",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Newton Wamiti",
                        fontWeight = FontWeight.SemiBold,
                        color = colorPrimary,
                        fontSize = 12.sp
                    )
                }
            }
            VerticalDivider()
            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {}
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = null,
                    tint = favourite
                )
                Column(
                    Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "My wishlist",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "WishList",
                        fontWeight = FontWeight.SemiBold,
                        color = favourite,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QrButton() {
    IconButton(
        onClick = {},
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_scan),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Composable
fun VerticalDivider() {
    Divider(
        color = Color(0xFFF1F1F1),
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
    )
}

@Composable
fun Promotions() {
    LazyRow(
        Modifier
            .height(160.dp)
            .padding(top = 20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PromotionItem(
                imagePainter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg")
            )
        }
        item {
            PromotionItem(
                imagePainter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_4.jpg")
            )
        }
    }
}

@Composable
fun PromotionItem(
    backgroundColor: Color = Color.Transparent,
    imagePainter: Painter
) {
    Card(
        Modifier.width(300.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = backgroundColor,
        elevation = 0.dp
    ) {
        Row {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                alignment = Alignment.CenterEnd,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryGridView() {
    val itemsCount = 19
    val columns = 4
    val itemHeight = 90.dp
    val spacing = 10.dp

    val rows = (itemsCount + columns - 1) / columns

    val totalHeight = (rows * itemHeight) + ((rows - 1) * spacing)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Product categories")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalHeight + 20.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(itemsCount) {
                    Card(
                        onClick = {},
                        modifier = Modifier
                            .padding(4.dp)
                            .height(itemHeight)
                            .width(itemHeight),
                        elevation = 6.dp,
                    ) {
                        Column(
                            Modifier

                                .padding(3.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg"),
                                contentDescription = "Category image",
                                modifier = Modifier
                                    .wrapContentSize()
                                    .wrapContentHeight()
                                    .wrapContentWidth(),
                            )
                            Spacer(modifier = Modifier.height(9.dp))
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = "Category",

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


@Composable
fun ProductCard() {
    Card(
        Modifier
            .width(160.dp)
    ) {
        Column(
            Modifier.padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(160.dp),
                painter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg"),
                contentDescription = "",
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "Product Name", fontWeight = FontWeight.Bold, maxLines = 2)
                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Price", color = colorPrimary)
                    Text(text = "SL: Quantity", color = Color.Red)
                }
            }
        }
    }

}


