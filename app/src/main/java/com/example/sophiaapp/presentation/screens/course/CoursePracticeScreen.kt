package com.example.sophiaapp.presentation.screens.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.BackButton
import com.example.sophiaapp.utils.localization.AppStrings

@Composable
fun CoursePracticeScreen(
    courseId:String,
    navController:NavHostController
){
    val courseTitle=when(courseId){
        "1" -> AppStrings.Course.COURSE_1_TITLE
        "2" -> AppStrings.Course.COURSE_2_TITLE
        "3" -> AppStrings.Course.COURSE_3_TITLE
        else -> AppStrings.Course.DEFAULT_COURSE_TITLE
    }
    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Row(
            verticalAlignment=Alignment.CenterVertically,
            modifier=Modifier
                .fillMaxWidth()
                .padding(bottom=24.dp)
        ){
            BackButton(onClick={
                navController.popBackStack(
                    Screen.CourseDetail.createRoute(courseId),
                    inclusive=false
                )
            })
            Spacer(modifier=Modifier.width(16.dp))

            Text(
                text=courseTitle + AppStrings.Course.PRACTICE_SUFFIX,
                style=MaterialTheme.typography.headlineSmall
            )
        }
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment=Alignment.CenterHorizontally,
            verticalArrangement=Arrangement.Center
        ){
            Text(
                text = AppStrings.Course.PRACTICE_PLACEHOLDER + courseTitle + "\"",
                style=MaterialTheme.typography.bodyLarge,
                modifier=Modifier.padding(bottom=16.dp)
            )
        }
    }
}