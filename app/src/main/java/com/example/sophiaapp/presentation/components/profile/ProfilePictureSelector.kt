package com.example.sophiaapp.presentation.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import coil.compose.AsyncImage
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R


@Composable
fun ProfilePictureSelector(
    imageUrl: String?,
    onImageSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource=remember{ MutableInteractionSource()}

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource=interactionSource,
                indication = rememberRipple(bounded = true,color=MaterialTheme.colorScheme.primary),
                onClick = onImageSelected)
    ) {

        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = AppStrings.Profile.PHOTO_PROFILE,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )
        }
        Image(
            painter=painterResource(id=if(imageUrl==null)R.drawable.photo_not
            else R.drawable.photo),
            contentDescription = null,
            modifier=Modifier.matchParentSize(),
            contentScale=ContentScale.FillBounds
        )

    }


}