package com.newton.mustmarket.core.sharedComposable.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ProductShimmer() {
    Box(modifier = Modifier.fillMaxHeight()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(color = MaterialTheme.colors.surface.copy(alpha = 0.4f))
                .padding(vertical = 4.dp)
        ) {
            Row {
                ShimmerAnimation(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(120.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxHeight(),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight(1f),
                ) {
                    ShimmerAnimation(
                        modifier = Modifier
                            .height(20.dp)
                            .width(50.dp)
                            .clip(shape = RoundedCornerShape(4.dp))

                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerAnimation(
                        modifier = Modifier
                            .height(20.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerAnimation(
                        modifier = Modifier
                            .height(20.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                            .fillMaxWidth()
                    )
                    Spacer(
                        Modifier
                            .height(10.dp)
                    )
                    ShimmerAnimation(
                        Modifier
                            .height(20.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                            .width(40.dp)
                    )

                }
            }


        }
    }
}