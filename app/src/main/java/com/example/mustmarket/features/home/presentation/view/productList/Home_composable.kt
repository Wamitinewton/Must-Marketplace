package com.example.mustmarket.features.home.presentation.view.productList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mustmarket.R
import com.example.mustmarket.features.home.domain.model.NetworkProduct


@Composable
 fun ProductListItem(
    product: NetworkProduct,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProductImage(
                imageUrl = product.imageUrl ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProductInfo(product = product)
        }
    }
}

@Composable
 fun ProductImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.no_image),
        error = painterResource(id = R.drawable.no_image)
    )
}

@Composable
 fun ProductInfo(
    product: NetworkProduct
) {
    Column {
        Text(
            text = product.name,
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "₹${product.price}",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = product.description,
            style = MaterialTheme.typography.body2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
 fun ProductsHeader() {
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
                style = MaterialTheme.typography.h6
            )
        }
    }
}