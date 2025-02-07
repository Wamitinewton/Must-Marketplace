package com.newton.mustmarket.features.onboarding.data

import androidx.compose.ui.graphics.Color
import com.newton.mustmarket.R

val pages = listOf(
    Page(
        id = 1,
        content = PageContent(R.raw.onboarding01, R.string.onboarding_title1, R.string.Onboarding1),
        color = Color(0xFF1E1E1E)
    ),
    Page(
        id = 2,
        content = PageContent(R.raw.onboarding02, R.string.onboarding_title2, R.string.Onboarding2),
        color = Color(0xFF1E1E1E)
    ),
    Page(
        id = 3,
        content = PageContent(R.raw.onboarding03, R.string.onboarding_title3, R.string.Onboarding3),
        color = Color(0xFF1E1E1E)
    )
)


data class Page(
    val id: Int,
    val content: PageContent,
    val color: Color
)

data class PageContent(
    val animation: Int,
    val title: Int,
    val text: Int
)
