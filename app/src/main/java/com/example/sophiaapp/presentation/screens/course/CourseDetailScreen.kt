package com.example.sophiaapp.presentation.screens.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sophiaapp.R
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.BackButton
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.utils.localization.AppStrings


@Composable
fun CourseDetailScreen(
    courseId:String,
    navController:NavHostController
){
    val courseTitle=when(courseId){
        "1"-> AppStrings.Course.COURSE_1_TITLE
        "2"-> AppStrings.Course.COURSE_2_TITLE
        "3"-> AppStrings.Course.COURSE_3_TITLE
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
            BackButton(onClick={navController.popBackStack()})
            Spacer(modifier=Modifier.width(16.dp))
            Text(
                text=courseTitle,
                style=MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier=Modifier.weight(1f))

        Column(
            modifier=Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment=Alignment.CenterHorizontally,
            verticalArrangement=Arrangement.spacedBy(16.dp)

        ){
            CourseOptionButton(
                text= AppStrings.Course.THEORY,
                iconRes=R.drawable.theory,
                onClick={
                    navController.navigate(Screen.CourseTheory.createRoute(courseId))
                },
                modifier=Modifier
                    .fillMaxWidth()
            )
            CourseOptionButton(
                text=AppStrings.Course.PRACTICE,
                iconRes = R.drawable.practice_icon,
                onClick={
                    navController.navigate(Screen.CoursePractice.createRoute(courseId))
                },
                modifier=Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier=Modifier.weight(1f))
    }
}

@Composable
fun CourseOptionButton(
    text:String,
    iconRes: Int,
    onClick: ()-> Unit,
    modifier:Modifier=Modifier
){
    Box(
        modifier=Modifier
            .height(56.dp)
            .clickable(onClick=onClick)
    ){
        Image(
            painter= painterResource(id=R.drawable.card_background_theory),
            contentDescription = null,
            modifier=Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier=Modifier
                .fillMaxSize()
                .padding(horizontal=16.dp),
            verticalAlignment=Alignment.CenterVertically
        ){
            Image(
                painter= painterResource(id=iconRes),
                contentDescription = null,
                modifier=Modifier.size(24.dp)
            )
            Spacer(modifier=Modifier.width(16.dp))

            Text(
                text=text,
                style=MaterialTheme.typography.bodyLarge,
                color=MaterialTheme.colorScheme.onSurface
            )
        }
    }
}