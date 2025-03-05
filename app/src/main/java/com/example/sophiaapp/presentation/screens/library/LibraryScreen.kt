package com.example.sophiaapp.presentation.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn



import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.sophiaapp.presentation.components.SearchBar
import com.example.sophiaapp.presentation.components.CourseCard
import com.example.sophiaapp.presentation.components.FilterBottomSheet



import androidx.compose.ui.tooling.preview.Preview



@Composable
fun LibraryScreen(){
    var showBottomSheet by remember {mutableStateOf(false)}
    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Text(
            text="Philosophy Basics",
            style=MaterialTheme.typography.headlineMedium,
            modifier=Modifier.padding(bottom=16.dp)
        )
        SearchBar(
            onFilterClick={showBottomSheet=true}
        )
        Spacer(modifier=Modifier.height(16.dp))

        LazyColumn(
            modifier=Modifier.fillMaxWidth(),
            verticalArrangement=Arrangement.spacedBy(16.dp)

        ){
            items(3){
                CourseCard(
                    title="Interactive Learning",
                    subtitle="Expert instructor",
                    onExploreClick={/*Обработка позже*/}
                )
            }

        }

    }
    FilterBottomSheet(
        showBottomSheet=showBottomSheet,
        onDismiss={showBottomSheet=false}
    )

}













@Preview(showBackground=true)
@Composable
fun LibraryScreenPreview(){
    LibraryScreen()
}