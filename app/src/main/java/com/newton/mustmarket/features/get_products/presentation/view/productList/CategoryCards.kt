package com.newton.mustmarket.features.get_products.presentation.view.productList

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newton.mustmarket.features.get_products.domain.model.categories.ProductCategory

@Composable
fun CategoryChipView(
    categories: List<ProductCategory>,
    selectedCategory: ProductCategory? = null,
    onCategoryClick: (ProductCategory) -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryChip(
    category: ProductCategory,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.surface
        },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colors.onPrimary
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
        },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        elevation = if (isSelected) 4.dp else 1.dp,
        backgroundColor = backgroundColor,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .defaultMinSize(minWidth = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor
                ),
                maxLines = 1
            )
        }
    }
}