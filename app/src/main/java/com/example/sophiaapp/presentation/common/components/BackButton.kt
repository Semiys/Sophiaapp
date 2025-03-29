package com.example.sophiaapp.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier:Modifier=Modifier

){
    IconButton(
        onClick=onClick,
        modifier=modifier
    ){
        Box(
            modifier=Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha=0.2f)),
            contentAlignment=Alignment.Center
        ){
            Icon(
                painter=painterResource(id=R.drawable.arrow_back),
                contentDescription=AppStrings.Common.BACK,
                modifier=Modifier.size(24.dp)
            )

        }
    }

}