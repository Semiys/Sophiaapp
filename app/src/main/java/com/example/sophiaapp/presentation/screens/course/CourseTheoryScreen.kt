package com.example.sophiaapp.presentation.screens.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun CourseTheoryScreen(
    courseId: String,
    navController: NavHostController
) {
    val courseTitle = when (courseId) {
        "1" -> AppStrings.Course.COURSE_1_TITLE
        "2" -> AppStrings.Course.COURSE_2_TITLE
        "3" -> AppStrings.Course.COURSE_3_TITLE
        "4" -> AppStrings.Course.COURSE_4_TITLE
        "5" -> AppStrings.Course.COURSE_5_TITLE
        "6" -> AppStrings.Course.COURSE_6_TITLE
        "7" -> AppStrings.Course.COURSE_7_TITLE
        "8" -> AppStrings.Course.COURSE_8_TITLE
        else -> AppStrings.Course.DEFAULT_COURSE_TITLE
    }
    val theoryText = when (courseId) {
        "1" -> AppStrings.Course.COURSE_1_DESCRIPTION
        "2" -> AppStrings.Course.COURSE_2_DESCRIPTION
        "3" -> AppStrings.Course.COURSE_3_DESCRIPTION
        "4" -> AppStrings.Course.COURSE_4_DESCRIPTION
        "5" -> AppStrings.Course.COURSE_5_DESCRIPTION
        "6" -> AppStrings.Course.COURSE_6_DESCRIPTION
        "7" -> AppStrings.Course.COURSE_7_DESCRIPTION
        "8" -> AppStrings.Course.COURSE_8_DESCRIPTION

        else -> AppStrings.Course.DEFAULT_COURSE_DESCRIPTION
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            BackButton(onClick = { navController.popBackStack() })
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = courseTitle + AppStrings.Course.THEORY_SUFFIX,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            TheoryCard(
                text = theoryText,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomButton(
                text = AppStrings.Course.CONTINUE_TO_PRACTICE,
                onClick = {
                    navController.navigate(Screen.CoursePractice.createRoute(courseId)) {
                        popUpTo(Screen.CourseDetail.createRoute(courseId))
                    }
                },
                backgroundRes = R.drawable.card_background_not,
                textColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)

            )
        }



    }
}

@Composable
fun TheoryCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier

    ) {
        Image(
            painter = painterResource(id = R.drawable.text_theory),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp)
        )
    }
}