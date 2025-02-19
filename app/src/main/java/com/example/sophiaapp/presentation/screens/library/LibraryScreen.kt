package com.example.sophiaapp.presentation.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier




@Composable
fun LibraryScreen(){
    Box(
        modifier=Modifier
            .fillMaxSize(),
        contentAlignment=Alignment.Center
    ){
        Text(
            text="Welcome, LibraryScreen",
            style=MaterialTheme.typography.titleLarge
        )
    }
}