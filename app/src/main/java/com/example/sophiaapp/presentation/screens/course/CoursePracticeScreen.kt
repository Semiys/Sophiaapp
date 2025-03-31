package com.example.sophiaapp.presentation.screens.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sophiaapp.data.PracticeRepository
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.BackButton
import com.example.sophiaapp.presentation.common.components.QuizScreen
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.domain.models.*
import androidx.compose.ui.text.style.TextAlign
@Composable
fun CoursePracticeScreen(
    courseId:String,
    navController:NavHostController
){
    val courseTitle=when(courseId){
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
    val practiceContent = PracticeRepository.getPracticeForCourse(courseId, courseTitle)


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

        when (practiceContent.type) {
            PracticeType.QUIZ -> {
                val quizContent = practiceContent as QuizContent
                QuizScreen(
                    quiz = quizContent.quiz,
                    onQuizComplete = { score, total ->
                        // Сохранение результата или показ поздравления
                    },
                    onReturnToCourse = {
                        navController.navigate(Screen.Library.route) {
                            popUpTo(Screen.Library.route) {
                                inclusive = false
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            PracticeType.PLACEHOLDER -> {
                val placeholderContent = practiceContent as PlaceholderContent
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = placeholderContent.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            // Когда добавите игры, можно будет раскомментировать и настроить под свои нужды
            /*
            PracticeType.GAME_1,
            PracticeType.GAME_2,
            PracticeType.GAME_3,
            PracticeType.GAME_4 -> {
                val gameContent = practiceContent as GameContent
                // Здесь будет ваш код для отображения игры
                // Например:
                GameScreen(
                    gameContent = gameContent,
                    onGameComplete = { /* Обработка завершения игры */ },
                    onBack = {
                        navController.popBackStack(
                            Screen.CourseDetail.createRoute(courseId),
                            inclusive = false
                        )
                    }
                )
            }
            */
            else -> {
                // Запасной вариант, если вдруг встретится неизвестный тип
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Этот тип контента пока не поддерживается",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}