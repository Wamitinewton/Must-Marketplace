package com.example.mustmarket.features.product.presentation.details

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mustmarket.features.product.domain.model.Product

@Composable
fun ProductDetailsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        backgroundColor = Color.White,
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
//                        navigator.popBackStack()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = if (true) {
                            painterResource(id = R.drawable.ic_dialog_email)
                        } else {
                            painterResource(id = R.drawable.ic_dialog_email)
                        },
                        tint = Color.Blue,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { paddingvalues ->
        Details(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = paddingvalues.calculateRightPadding(layoutDirection = LayoutDirection.Ltr),
                    vertical = paddingvalues.calculateTopPadding()
                )
        )
    }
}


@Composable
fun Details(modifier: Modifier = Modifier) {
    Column {
        Box(modifier = modifier.weight(1f), contentAlignment = Alignment.Center) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = "https://images.unsplash.com/photo-1710905219584-8521769e3678?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8bWFjYm9vayUyMG0zfGVufDB8fDB8fHww")
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_menu_sort_alphabetically)
                        }).build()
                ),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Inside
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = modifier
                .fillMaxWidth()
                .weight(2f),
            elevation = 0.dp,
            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
//            backgroundColor = MainWhiteColor
        ) {

            Box(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Macbook M3",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "MacBook Pro M3 Pro 16 inch price in Kenya is KSh299,000. The key features of this laptop include 16.2 inches Liquid Retina display, powerful Apple M3 Pro chip, a powerful battery that can last for up to 22 hours on a single charge, Backlit Magic Keyboard with Touch ID and 140W USB-C Power Adapter.",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )

                }
                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "KSh299,000",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
//                            backgroundColor = YellowMain,
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            text = "Add to Cart"
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun DetailsPrev() {
    ProductDetailsScreen()
}