package com.example.mustmarket.core.SharedComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mustmarket.core.util.Constants
import com.example.mustmarket.ui.theme.colorPrimary
import com.example.mustmarket.ui.theme.light_gray


@Composable
fun TopProduct() {
    Column(
        Modifier
            .background(Color.White)
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Featured Products")
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "See more..")
                Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = "")

            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(5){
                ProductItem()
            }
        }
    }

}




@Composable
fun ProductItem() {
    Box(
        modifier = Modifier
            .border(1.dp, light_gray, RectangleShape)
            .padding(10.dp)
            .width(80.dp)
            .clickable{}
    ) {
        Column() {
            Image(
                painter = rememberAsyncImagePainter("https://theme.hstatic.net/200000420363/1001015796/14/banner_right_3.jpg"
                ),
                contentDescription = "product",
                Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = "product name",
                maxLines = 2,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = Constants.formatPrice(200.0) + "$",
                maxLines = 2,
                fontSize = 12.sp,
                color = colorPrimary
            )
        }
    }

}