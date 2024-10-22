package com.example.mustmarket.features.home.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun AllProducts() {
    val exampleProducts = listOf(
        Product(
            name = "Apple",
            price = 1.99,
            imageUrl = "https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg",
            description = "This is just an example of what a product looks like"
        ),
        Product(
            name = "Banana",
            price = 0.99,
            imageUrl = "https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg",
            description = "This is just an example of what a product looks like"
        ),
        Product(
            name = "Orange",
            price = 2.49,
            imageUrl = "https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg",
            description = "This is just an example of what a product looks like"
        )
    )

    Column {
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
                    text = "All Products",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        exampleProducts.forEach { product ->
            ProductCard(product)
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxHeight(),

                ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.h2.copy(
                        fontSize = 21.sp
                    )
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

data class Product(
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)
