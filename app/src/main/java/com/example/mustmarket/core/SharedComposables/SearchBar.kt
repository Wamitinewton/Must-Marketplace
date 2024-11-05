package com.example.mustmarket.core.SharedComposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mustmarket.R
import com.example.mustmarket.features.home.presentation.state.HomeScreenEvent
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBar(
    autoFocus: Boolean,
    onQueryChange: (HomeScreenEvent.Search) -> Unit,
    onClearQuery: () -> Unit,
    query: String,
    onSearch: () -> Unit = {},
    isSearchActive: Boolean,

    ) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            .clip(CircleShape)
            .fillMaxWidth()
            .height(54.dp)
    ) {


        TextField(
            value = query,
            onValueChange = { newValue ->
                onQueryChange(HomeScreenEvent.Search(newValue.trim()))
            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester = focusRequester),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Search products...",
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            },
            colors = textFieldColors(
                textColor = ThemeUtils.AppColors.Surface.themed(),
                backgroundColor = ThemeUtils.AppColors.Text.themed(),
                disabledTextColor = Color.LightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearch()
                },

                ),
            trailingIcon = {

                Row {
                    AnimatedVisibility(visible = query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                focusManager.clearFocus()
                                onClearQuery()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                tint = Color.Black,
                                contentDescription = null
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            onSearch()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            tint = Color.Black,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}