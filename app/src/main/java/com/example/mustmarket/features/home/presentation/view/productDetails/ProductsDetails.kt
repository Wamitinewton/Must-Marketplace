package com.example.mustmarket.features.home.presentation.view.productDetails

import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mustmarket.core.sharedComposable.CustomImageLoader
import com.example.mustmarket.core.util.Constants.formatPrice
import com.example.mustmarket.core.util.SingleToastManager
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import com.example.mustmarket.features.home.presentation.state.BookmarkEvent
import com.example.mustmarket.features.home.presentation.viewmodels.BookmarksViewModel
import com.example.mustmarket.features.home.presentation.viewmodels.SharedViewModel
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun ProductDetailsScreen(
    onBackPressed: () -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel(),
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val details = sharedViewModel.details

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BookmarkEvent.Success -> {
                    SingleToastManager.showToast(
                        context = context,
                        message = event.message,
                        scope = scope,
                    )
                }
                is BookmarkEvent.Error -> {
                    event.message?.let { errorMessage ->
                        SingleToastManager.showToast(
                            context = context,
                            message = errorMessage,
                            scope = scope,
                        )
                    }
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (details) {
            is NetworkProduct->{
                ProductDetailsContent(
                    product = details,
                    onBackPressed = onBackPressed,
                    bookmarksViewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}


@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProductDetailsContent(
    product: NetworkProduct,
    onBackPressed: () -> Unit,
    bookmarksViewModel: BookmarksViewModel,
    navController: NavController
) {
    val bookmarkStatuses = bookmarksViewModel.bookmarkStatusUpdates.collectAsState()
    val isBookmarked = bookmarkStatuses.value[product.id] ?: false
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var progress by remember { mutableFloatStateOf(0f) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    val topBarHeight = 56.dp
    val topPadding = 16.dp

    val motionScene = remember {
        MotionScene {
            val card = createRefFor("card")
            val image = createRefFor("image")
            val cardContent = createRefFor("cardContent")

            defaultTransition(
                from = constraintSet {
                    constrain(image) {
                        width = Dimension.fillToConstraints
                        height = Dimension.value(350.dp)
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }

                    constrain(card) {
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        top.linkTo(image.bottom, margin = (-17).dp)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    constrain(cardContent) {
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        top.linkTo(card.top)
                        bottom.linkTo(card.bottom)
                        start.linkTo(card.start)
                        end.linkTo(card.end)
                    }
                },
                to = constraintSet {
                    constrain(image) {
                        width = Dimension.fillToConstraints
                        height = Dimension.value(0.dp)
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    constrain(card) {
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    constrain(cardContent) {
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        top.linkTo(card.top, margin = (topBarHeight + topPadding))
                        bottom.linkTo(card.bottom)
                        start.linkTo(card.start)
                        end.linkTo(card.end)
                    }
                }
            ) {
                keyAttributes(image) {
                    frame(0) {
                        alpha = 1f
                    }
                    frame(100) {
                        alpha = 0f
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        product.name,
                        style = MaterialTheme.typography.h6.copy(
                            color = ThemeUtils.AppColors.Text.themed(),
                            fontSize = 22.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { bookmarksViewModel.toggleBookmarkStatus(product) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) MaterialTheme.colors.primary else Color.Gray
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        MotionLayout(
            motionScene = motionScene,
            progress = progress,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            dragOffset += dragAmount
                            val newProgress = (progress - (dragAmount / 1000f)).coerceIn(0f, 1f)
                            progress = newProgress
                            change.consume()

                        },
                        onDragEnd = {
                            scope.launch {
                                if (abs(dragOffset) > 100) {
                                    if (dragOffset < 0) {
                                        animate(
                                            initialValue = progress,
                                            targetValue = 1f
                                        ) { value, _ ->
                                            progress = value
                                        }
                                    } else {
                                        animate(
                                            initialValue = progress,
                                            targetValue = 0f
                                        ) { value, _ ->
                                            progress = value

                                        }
                                    }
                                } else {
                                    animate(
                                        initialValue = progress,
                                        targetValue = if (progress < 0.5f) 0f else 1f
                                    ) { value, _ ->
                                        progress = value
                                    }
                                }
                                dragOffset = 0f
                            }
                        }
                    )

                }

        ) {
            Box(
                modifier = Modifier
                    .layoutId("image")
                    .padding(top = 20.dp)

            ) {
                CustomImageLoader(
                    imageUrl = product.images[0],
                )
            }

            Box(
                modifier = Modifier
                    .layoutId("card")
                    .clip(RectangleShape)
                    .background(MaterialTheme.colors.surface)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                        shape = RectangleShape
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .align(Alignment.CenterHorizontally)

                    )
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier
                                .size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Meru, Nchiru",
                            style = MaterialTheme.typography.caption
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatPrice(product.price) + "Ksh",
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.secondaryVariant,
                                fontSize = 20.sp
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "4.5",
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.body1.copy(
                            color = ThemeUtils.AppColors.SecondaryText.themed(),
                            lineHeight = 24.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    ContactSeller()
                }
            }

        }
    }
}

@Composable
fun ContactSeller() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(150.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email-seller"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Email Seller", color = MaterialTheme.colors.primary)
            }
        }
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(150.dp)
                .clickable(
                    onClick = {}
                )
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    tint = Color.Black,
                    contentDescription = "Email-seller"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Call the Seller")
            }
        }
    }
}