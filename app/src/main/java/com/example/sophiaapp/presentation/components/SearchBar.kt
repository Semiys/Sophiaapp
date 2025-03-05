package com.example.sophiaapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
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




@Composable
fun SearchBar(
    modifier:Modifier=Modifier,
    onFilterClick:()->Unit
){
    Row(
        modifier=Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal=16.dp),
        verticalAlignment=Alignment.CenterVertically,
        horizontalArrangement=Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment=Alignment.CenterVertically
        ){
            Icon(
                imageVector=Icons.Default.Search,
                contentDescription="Search",
                tint=MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier=Modifier.width(8.dp))

            Text(
                text="Search Philosophy Topics",
                style=MaterialTheme.typography.bodyLarge,
                color=MaterialTheme.colorScheme.onSurfaceVariant

            )
        }
        IconButton(
            onClick=onFilterClick
        ){
            Icon(
                imageVector=Icons.Default.KeyboardArrowUp,
                contentDescription="Filter",
                tint=MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}