package com.example.sophiaapp.presentation.screens.progress

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.utils.localization.AppStrings



@Composable
fun ProgressScreen(paddingValues: PaddingValues = PaddingValues()){
    LazyColumn(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            Text(
                text=AppStrings.Progress.SCREEN_TITLE,
                style=MaterialTheme.typography.headlineMedium,
                modifier=Modifier.padding(bottom=16.dp)
            )
        }
    }
}

