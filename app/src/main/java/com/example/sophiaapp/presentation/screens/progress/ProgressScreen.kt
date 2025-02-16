package com.example.sophiaapp.presentation.screens.progress

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment



@Composable
fun ProgressScreen(){
    Box(
        modifier=Modifier
            .fillMaxSize(),
            contentAlignment=Alignment.Center
    ){
        Text(
            text="Welcome, ProgressScreen",
            style=MaterialTheme.typography.titleLarge
        )
    }
}