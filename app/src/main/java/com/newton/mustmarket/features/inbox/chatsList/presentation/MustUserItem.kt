package com.newton.mustmarket.features.inbox.chatsList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun MustUserItem(
    user: AuthedUser,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = 6.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val profileImage = user.number ?: user.name // change it to images @googleauthProfile & @merchantStoreimage if possible else delete the code
            if (profileImage != null) {
                AsyncImage(
                    model = profileImage,
                    contentDescription = "user avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.email.firstOrNull()?.uppercase() ?: "?",
                        color = ThemeUtils.AppColors.Text.themed(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))


            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.h6,
                    color = ThemeUtils.AppColors.Text.themed()
                )

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.body2,
                    color = ThemeUtils.AppColors.Text.themed()
                )
            }
        }
    }
}