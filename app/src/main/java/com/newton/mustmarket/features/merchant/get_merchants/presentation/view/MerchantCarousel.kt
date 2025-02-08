package com.newton.mustmarket.features.merchant.get_merchants.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.features.merchant.get_merchants.domain.model.GetMerchantsData
import kotlinx.coroutines.delay

@Composable
fun MerchantCarousel(
    modifier: Modifier = Modifier,
    merchants: List<GetMerchantsData>,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    onMerchantClick: (GetMerchantsData) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { merchants.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % merchants.size
            pagerState.animateScrollToPage(nextPage)
            onIndexChange(nextPage)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Popular merchants",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) { page ->
            MerchantCard(
                merchantsData = merchants[page],
                onClick = { onMerchantClick(merchants[page]) }
            )
        }

        Row(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(merchants.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
                }
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}