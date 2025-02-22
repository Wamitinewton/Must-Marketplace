package com.newton.mustmarket.features.onboarding.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.newton.mustmarket.core.adaptableLayout.ResponsiveSizeUtil
import com.newton.mustmarket.core.sharedComposable.LoopReverseLottieLoader
import com.newton.mustmarket.features.onboarding.data.pages
import com.newton.mustmarket.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController
) {
    val pagerState = rememberPagerState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        BubblePager(
            pagerState = pagerState,
            pageCount = pages.size,
            modifier = Modifier.fillMaxSize(),
            bubbleColors = pages.map { page ->
                if (pagerState.currentPage == page.id - 1) {
                    page.color
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                }
            },
            onGetStartedClick = {
                navController.popBackStack()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route)
                }
            }
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoopReverseLottieLoader(
                    lottieFile = pages[page].content.animation,
                    modifier = Modifier.size(ResponsiveSizeUtil.responsiveDimension(baseDimension = 300.dp))
                )

                Text(
                    text = stringResource(id = pages[page].content.title),
                    style = MaterialTheme.typography.h2,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    softWrap = true,
                    modifier = Modifier.padding(horizontal = ResponsiveSizeUtil.responsivePadding(basePadding = 20.dp))
                )
                Spacer(modifier = Modifier.height(ResponsiveSizeUtil.responsivePadding(20.dp)))
                Text(
                    text = stringResource(id = pages[page].content.text),
                    style = MaterialTheme.typography.body1.copy(

                    ),
                    textAlign = TextAlign.Center,
                    softWrap = true,
                    modifier = Modifier.padding(horizontal = ResponsiveSizeUtil.responsivePadding(basePadding = 20.dp))
                )
            }
        }
        PagerTopAppBar(
            page = pagerState.currentPage,
            modifier = Modifier
                .wrapContentSize()
                .statusBarsPadding()
        )
    }
}


@Composable
fun PagerTopAppBar(page: Int, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu icon",
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        contentColor = if (page == 2) Color.Black else Color.White,
        modifier = modifier
    )
}