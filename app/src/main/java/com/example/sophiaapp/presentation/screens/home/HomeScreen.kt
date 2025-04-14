package com.example.sophiaapp.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.StrokeCap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sophiaapp.R
import com.example.sophiaapp.data.local.database.AppDatabase
import com.example.sophiaapp.data.repository.FirebaseAuthRepository
import com.example.sophiaapp.data.repository.UserProgressRepository
import com.example.sophiaapp.domain.models.LocationType
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.viewmodels.UserProgressViewModel
import com.example.sophiaapp.utils.localization.AppStrings

@Composable
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavController,
    progressViewModel: UserProgressViewModel = viewModel(
        factory = UserProgressViewModel.Factory(
            progressRepository = UserProgressRepository(
                userProgressDao = AppDatabase.getInstance(LocalContext.current).userProgressDao(),
                firebaseRepository = FirebaseAuthRepository()
            ),
            firebaseRepository = FirebaseAuthRepository()
        )
    )
) {
    // Получаем текущий прогресс пользователя
    val userProgress by progressViewModel.userProgress.collectAsState()

    // Получаем количество выполненных теорий и практик напрямую из ViewModel
    val completedTheoryCount = userProgress?.courseProgress?.count { (courseId, data) ->
        courseId.toIntOrNull() in 1..8 && progressViewModel.isCourseTheoryCompleted(courseId)
    } ?: 0

    val completedPracticeCount = userProgress?.courseProgress?.count { (courseId, data) ->
        courseId.toIntOrNull() in 1..8 && progressViewModel.isCoursePracticeCompleted(courseId)
    } ?: 0

    // Рассчитываем общий прогресс (каждый курс содержит теорию и практику)
    val totalCourses = 8 // Общее количество реализованных курсов
    val totalSections = totalCourses * 2 // Общее количество разделов (теория + практика для каждого курса)
    val completedSections = completedTheoryCount + completedPracticeCount

    // Рассчитываем процент завершения (от 0.0f до 1.0f)
    val progress = completedSections.toFloat() / totalSections

    val detailedProgress = progressViewModel.getDetailedProgress()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                text = AppStrings.HomeScreen.WELCOME_PHILOSOPHER,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
            )
            Text(
                text = AppStrings.HomeScreen.EXPLORE_PHILOSOPHY,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.card_background),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.HomeScreen.DIVE_INTO_THE_DEPTHS,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Image(
                            painter = painterResource(id = R.drawable.book_new),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    // Удалили чекбокс, оставили только текст
                    Text(
                        text = AppStrings.HomeScreen.UNLOCK_NEW,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Прогресс-бар с увеличенной толщиной
                    LinearProgressIndicator(
                        progress = { detailedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        strokeCap = StrokeCap.Round
                    )

                    // Обновляем текст с процентами
                    Text(
                        text = "${(detailedProgress * 100).toInt()}% (общий прогресс с учетом правильных ответов)",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        item {
            Text(
                text = AppStrings.HomeScreen.PONDER_THE_MYSTERIES,
                style = MaterialTheme.typography.titleMedium
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.card_background),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = AppStrings.HomeScreen.PHILOSOPHICAL_TOOLS,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = AppStrings.HomeScreen.JOIN_GLOBAL,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.book_new),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    // Обновленная кнопка "Begin Now" с навигацией к последнему местоположению
                    CustomButton(
                        text = AppStrings.HomeScreen.BEGIN_NOW,
                        onClick = {
                            val lastLocation = progressViewModel.getLastLocation()
                            val courseId = lastLocation.courseId

                            when (lastLocation.type) {
                                LocationType.THEORY -> {
                                    if (progressViewModel.isCourseTheoryCompleted(courseId)) {
                                        navController.navigate(Screen.CoursePractice.createRoute(courseId)) {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        navController.navigate(Screen.CourseTheory.createRoute(courseId)) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                                LocationType.PRACTICE -> {
                                    if (progressViewModel.isCoursePracticeCompleted(courseId)) {
                                        val nextCourseId = (courseId.toIntOrNull()?.plus(1) ?: 1).toString()
                                        navController.navigate(Screen.CourseTheory.createRoute(nextCourseId)) {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        navController.navigate(Screen.CoursePractice.createRoute(courseId)) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                                else -> {
                                    navController.navigate(Screen.Library.route) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        backgroundRes = R.drawable.continue_button,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}