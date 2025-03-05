package com.example.sophiaapp.presentation.components

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



import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment


import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button



@Composable
fun CourseCard(
    title: String,
    subtitle: String,
    onExploreClick:() -> Unit
){
    Card(
        modifier=Modifier
            .fillMaxWidth()
            .height(120.dp),
        elevation=CardDefaults.cardElevation(
            defaultElevation= 4.dp
        ),
        shape=RoundedCornerShape(16.dp)
    ){
        Row(
            modifier=Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement=Arrangement.SpaceBetween

        ){
            Box(
                modifier=Modifier
                    .size(88.dp)
                    .background(
                        color=MaterialTheme.colorScheme.surfaceVariant,
                        shape=RoundedCornerShape(8.dp)
                    )
            )
            Column(
                modifier=Modifier
                    .weight(1f)
                    .padding(horizontal=16.dp),
                verticalArrangement=Arrangement.Center
            ){
                Text(
                    text=title,
                    style=MaterialTheme.typography.titleMedium
                )
                Text(
                    text=subtitle,
                    style=MaterialTheme.typography.bodyMedium,
                    color=MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
            Button(
                onClick=onExploreClick,
                modifier=Modifier
                    .align(Alignment.Bottom)
            ){
                Text("Explore")
            }

        }

    }
}




