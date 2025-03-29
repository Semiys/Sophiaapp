package com.example.sophiaapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton



import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

import androidx.compose.ui.draw.clip

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.R


@Composable
fun SearchBar(
    modifier:Modifier=Modifier,
    onFilterClick:()->Unit
){
    Box(
        modifier=Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center

    ) {
        Image(
            painter= painterResource(id=R.drawable.continue_button),
            contentDescription = null,
            modifier=Modifier.matchParentSize(),
            contentScale= ContentScale.FillBounds

        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier=Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = AppStrings.SearchBar.SEARCH,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = AppStrings.SearchBar.SEARCH_PHILOSOPHY_TOPICS,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )
            }
            IconButton(
                onClick = onFilterClick,
                modifier=Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = AppStrings.FilterBottomSheet.FILTER,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
    }
}