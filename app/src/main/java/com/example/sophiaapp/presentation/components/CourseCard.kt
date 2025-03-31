package com.example.sophiaapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width


import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment


import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R
import com.example.sophiaapp.presentation.common.components.CustomButton


@Composable
fun CourseCard(
    courseId:String,
    title: String,
    subtitle: String,
    iconRes:Int=R.drawable.stars_library,
    onExploreClick:(String) -> Unit
){
    Box(
        modifier=Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
    ){
        Image(
            painter= painterResource(id=R.drawable.card_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier=Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement=Arrangement.SpaceBetween

        ){
            Column(
                modifier=Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement=Arrangement.Center
            ){
                Image(
                    painter= painterResource(id=iconRes),
                    contentDescription=null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(bottom=8.dp)
                )
                Text(
                    text=title,
                    style=MaterialTheme.typography.titleMedium,
                    color=MaterialTheme.colorScheme.onSurface,
                    maxLines=2
                )
                Text(
                    text=subtitle,
                    style=MaterialTheme.typography.bodyMedium,
                    color=MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
            Box(
                modifier=Modifier
                    .align(Alignment.Bottom)
            ){
                CustomButton(
                    text=AppStrings.CourseCard.EXPLORE,
                    onClick={onExploreClick(courseId)},
                    backgroundRes = R.drawable.auth_sign,
                    textColor=MaterialTheme.colorScheme.primary,
                    modifier=Modifier
                        .width(120.dp)
                        .height(40.dp)
                )
            }

        }

    }
}




