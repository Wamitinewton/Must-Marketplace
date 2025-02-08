package com.newton.mustmarket.features.merchant.get_merchants.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.core.sharedComposable.CustomImageLoader
import com.newton.mustmarket.features.merchant.get_merchants.domain.model.GetMerchantsData

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MerchantCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    merchantsData: GetMerchantsData
) {
   Card(
       modifier = modifier
           .fillMaxWidth()
           .height(200.dp),
       shape = RoundedCornerShape(12.dp),
       elevation = 4.dp,
       onClick = onClick
   ) {
       Box {
          CustomImageLoader(
              imageUrl = merchantsData.banner
          )

           Box(
               modifier = Modifier
                   .fillMaxSize()
                   .background(
                       Brush.verticalGradient(
                           colors = listOf(
                               Color.Transparent,
                               Color.Black.copy(alpha = 0.7f)
                           )
                       )
                   )
           )

           Column(
               modifier = Modifier.align(Alignment.BottomStart)
                   .padding(16.dp)
           ) {
               Text(
                   text = merchantsData.name,
                   style = MaterialTheme.typography.h6.copy(
                       color = Color.White,
                       fontWeight = FontWeight.Bold
                   )
               )
               Text(
                   text = merchantsData.description,
                   style = MaterialTheme.typography.body2.copy(
                       color = Color.White.copy(alpha = 0.8f)
                   ),
                   maxLines = 2,
                   overflow = TextOverflow.Ellipsis,
                   modifier = Modifier.padding(top = 4.dp)
               )

               Row(
                   modifier = Modifier.padding(top = 8.dp),
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Text(
                       text = "${merchantsData.product?.size ?: 0} products",
                       style = MaterialTheme.typography.caption.copy(
                           color = Color.White.copy(alpha = 0.7f)
                       )
                   )

                   Spacer(modifier = Modifier.width(16.dp))

                   Text(
                       text = merchantsData.address,
                       style = MaterialTheme.typography.caption.copy(
                           color = Color.White.copy(alpha = 0.7f)
                       ),
                       maxLines = 1,
                       overflow = TextOverflow.Ellipsis
                   )
               }
           }
       }
   }
}