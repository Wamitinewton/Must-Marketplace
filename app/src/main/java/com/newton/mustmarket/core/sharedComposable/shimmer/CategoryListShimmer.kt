package com.newton.mustmarket.core.sharedComposable.shimmer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryListShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                count =8,
            ) {
                Box(modifier = Modifier.height(90.dp).width(90.dp)){
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        ShimmerAnimation(Modifier.height(60.dp).fillMaxWidth())
                        Spacer(Modifier.height(10.dp))
                        ShimmerAnimation(Modifier.height(10.dp).width(30.dp))
                    }
                }
            }
        }
    }
}