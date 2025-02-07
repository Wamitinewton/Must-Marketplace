package com.newton.mustmarket.core.sharedComposable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.R
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBar(
    autoFocus: Boolean,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    query: String,
    onSearch: () -> Unit = {},
    isSearchActive: Boolean,
    onSearchNavigationClick: () -> Unit
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
            .fillMaxWidth()
            .height(50.dp)
            .clip(RectangleShape)
            .background(Color.White)
    ) {

        TextField(
            value = query,
            onValueChange = { newValue ->
                onQueryChange(newValue.trim())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .focusRequester(focusRequester = focusRequester),

            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = onSearchNavigationClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "navigate to home",
                        tint = ThemeUtils.AppColors.Surface.themed()
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Search...",
                    style = MaterialTheme.typography.caption,
                )
            },
            colors = textFieldColors(
                textColor = ThemeUtils.AppColors.Surface.themed(),
                backgroundColor = Color.Transparent,
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

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
