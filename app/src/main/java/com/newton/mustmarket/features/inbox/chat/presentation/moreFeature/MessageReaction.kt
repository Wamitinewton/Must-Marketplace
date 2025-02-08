package com.newton.mustmarket.features.inbox.chat.presentation.moreFeature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.features.inbox.chat.model.ChatMessage

@Composable
fun ReactionOverlay(
    message: ChatMessage,
    onReactionSelected: (String) -> Unit
) {
    var showReactions by remember { mutableStateOf(false) }

    Box(modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onLongPress = { showReactions = true },
            onTap = { showReactions = false }
        )
    }) {
        if (showReactions) {
            ReactionPicker(
                modifier = Modifier.align(Alignment.TopStart),
                onReactionSelected = {
                    onReactionSelected(it)
                    showReactions = false
                }
            )
        }
    }
}

@Composable
private fun ReactionPicker(
    modifier: Modifier = Modifier,
    onReactionSelected: (String) -> Unit
) {
    val reactions = listOf("❤️", "😂", "😮", "😢", "👍", "👎")

    Row(
        modifier = modifier
            .background(Color.White, CircleShape)
            .shadow(4.dp, CircleShape)
            .padding(8.dp)
    ) {
        reactions.forEach { reaction ->
            Text(
                text = reaction,
                modifier = Modifier
                    .clickable { onReactionSelected(reaction) }
                    .padding(4.dp)
                    .size(32.dp)
            )
        }
    }
}