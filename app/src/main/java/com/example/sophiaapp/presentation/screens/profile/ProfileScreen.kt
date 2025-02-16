package com.example.sophiaapp.presentation.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(){
    Box(
        modifier=Modifier
            .fillMaxSize(),
            contentAlignment=Alignment.Center
    ){
        Text(
            text="Welcome, ProfileScreen",
            style= MaterialTheme.typography.titleLarge
        )

    }
}


