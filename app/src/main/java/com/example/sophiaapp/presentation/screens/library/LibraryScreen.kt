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
import androidx.compose.foundation.layout.PaddingValues



import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.sophiaapp.presentation.components.SearchBar
import com.example.sophiaapp.presentation.components.CourseCard
import com.example.sophiaapp.presentation.components.FilterBottomSheet
import com.example.sophiaapp.navigation.Screen


import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.sophiaapp.utils.localization.AppStrings


@Composable
fun LibraryScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController:NavHostController
){
    var showBottomSheet by remember {mutableStateOf(false)}
    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ){
        Text(
            text=AppStrings.Library.PHILOSOPHY_BASICS,
            style=MaterialTheme.typography.headlineMedium,
            modifier=Modifier.padding(top=16.dp,bottom=16.dp)
        )
        SearchBar(
            onFilterClick={showBottomSheet=true}
        )
        Spacer(modifier=Modifier.height(16.dp))

        LazyColumn(
            modifier=Modifier.fillMaxWidth(),
            verticalArrangement=Arrangement.spacedBy(16.dp)

        ){
            items(3){index ->
                val courseId=(index+1).toString()
                CourseCard(
                    courseId=courseId,
                    title=when(index){
                        0 -> AppStrings.Course.COURSE_1_TITLE
                        1 -> AppStrings.Course.COURSE_2_TITLE
                        else -> AppStrings.Course.COURSE_3_TITLE
                    },
                    subtitle=AppStrings.Library.EXPERT_INSTRUCTOR,
                    onExploreClick={id ->
                        navController.navigate(Screen.CourseDetail.createRoute(id))
                    }
                )
            }
            item{
                Spacer(modifier=Modifier.height(60.dp))
            }

        }

    }
    FilterBottomSheet(
        showBottomSheet=showBottomSheet,
        onDismiss={showBottomSheet=false}
    )

}












/*
@Preview(showBackground=true)
@Composable
fun LibraryScreenPreview(){
    LibraryScreen()
}
*/