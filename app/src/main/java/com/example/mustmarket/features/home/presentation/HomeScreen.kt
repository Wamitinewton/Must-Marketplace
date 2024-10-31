package com.example.mustmarket.features.home.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.AppBarPrimary
import com.example.mustmarket.core.SharedComposables.TopProduct
import com.example.mustmarket.ui.theme.colorPrimary
import com.example.mustmarket.ui.theme.favourite

@Composable
fun HomeScreen(
    navController: NavController
) {
    Box {

        Content()
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            AppBarPrimary()
        }
        item(span = { GridItemSpan(2) }) {
            HeaderBar()
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .padding(100.dp)
            )
            Promotions()
        }
        item(span = { GridItemSpan(2) }){
            Spacer(modifier = Modifier.height(16.dp))
            CategorySection()
        }

        item(span = { GridItemSpan(2) }){
            TopProduct()
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(16.dp))
            BestSellerSection()
        }
        item(span = { GridItemSpan(2) }) {
            ProductCard()
        }

    }
}


@Composable
fun HeaderBar(modifier: Modifier = Modifier) {
    Card(
        Modifier
            .height(64.dp)
            .padding(horizontal = 16.dp),
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

@Composable
fun CategorySection() {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Product Categories", style = MaterialTheme.typography.h6)
            TextButton(onClick = {}) {
                Text(text = "See More")
            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(5) {
                CategoryButton()
            }
        }
    }
}


@Composable
fun CategoryButton() {
    Column(
        Modifier.width(72.dp)
    ) {
        Box(
            Modifier
                .size(72.dp)
                .background(
                    color = Color(0xffEAFAF1),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(18.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "Category",
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp),
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
    }
}

@Composable
fun BestSellerSection() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Best sellers", style = MaterialTheme.typography.h6)
        TextButton(onClick = {}) {
            Text(text = "See all..", color = colorPrimary)
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

