package com.example.sophiaapp.presentation.components.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sophiaapp.R
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.viewmodels.FillInBlankGameViewModel
import com.example.sophiaapp.utils.localization.AppStrings

@Composable
fun FillInBlankGameScreen(
    gameId: String,
    navController: NavHostController,
    courseId: String,
    onGameCompleted: (Int, Int) -> Unit,
    viewModel: FillInBlankGameViewModel = viewModel(factory = FillInBlankGameViewModel.Factory(gameId))
) {
    val game by viewModel.game.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val answersChecked by viewModel.answersChecked.collectAsState()
    val answerResults by viewModel.answerResults.collectAsState()
    val score by viewModel.score.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Заголовок и описание игры
        game?.let { currentGame ->
            Text(
                text = currentGame.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = currentGame.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Список вопросов
            currentGame.questions.forEach { question ->
                FillInBlankQuestionItem(
                    question = question,
                    userAnswer = userAnswers[question.id] ?: "",
                    isAnswerChecked = answersChecked,
                    isCorrect = answerResults[question.id] ?: false,
                    onAnswerChanged = { answer ->
                        viewModel.updateAnswer(question.id, answer)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Кнопки для проверки и сброса
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomButton(
                    onClick = { viewModel.resetGame() },
                    text = "Сбросить",
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    backgroundRes = R.drawable.card_background,
                    textColor = Color.Black
                )

                CustomButton(
                    onClick = { if (!answersChecked) viewModel.checkAnswers() },
                    text = "Проверить",
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    backgroundRes = R.drawable.card_background,
                    textColor = Color.Black
                )

            }
            
            // Результаты и кнопка возврата
            if (answersChecked) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE6F0FF)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ваш результат: $score из ${currentGame.questions.size}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = when {
                                score == currentGame.questions.size -> "Отлично! Вы ответили на все вопросы правильно!"
                                score > currentGame.questions.size / 2 -> "Хорошо! Вы ответили правильно на большинство вопросов."
                                else -> "Попробуйте еще раз, чтобы улучшить свой результат."
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        CustomButton(
                            onClick = {
                                onGameCompleted(score, currentGame.questions.size)
                                navController.navigate(Screen.Library.route) {
                                    popUpTo(Screen.Library.route) {
                                        inclusive = false
                                    }
                                }
                            },
                            text = "Вернуться к курсу",
                            backgroundRes = R.drawable.card_background,
                            textColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillInBlankQuestionItem(
    question: com.example.sophiaapp.domain.models.FillInBlankQuestion,
    userAnswer: String,
    isAnswerChecked: Boolean,
    isCorrect: Boolean,
    onAnswerChanged: (String) -> Unit
) {
    val borderColor = when {
        !isAnswerChecked -> Color(0xFFE0E0E0)
        isCorrect -> Color(0xFF4CAF50)
        else -> Color(0xFFE57373)
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Обработка текста вопроса с пропуском
            val displayText = question.text.replace("[BLANK]", "_____")
            
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Поле для ввода ответа
            OutlinedTextField(
                value = userAnswer,
                onValueChange = { onAnswerChanged(it) },
                label = { Text("Ваш ответ") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = !isAnswerChecked,
                isError = isAnswerChecked && !isCorrect,
                // ЗАМЕНИТЬ ЭТОТ БЛОК
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = when {
                        !isAnswerChecked -> MaterialTheme.colorScheme.primary
                        isCorrect -> Color(0xFF4CAF50)
                        else -> Color(0xFFE57373)
                    },
                    unfocusedBorderColor = when {
                        !isAnswerChecked -> Color.Gray
                        isCorrect -> Color(0xFF4CAF50)
                        else -> Color(0xFFE57373)
                    },
                    // Добавляем параметры для цвета контейнера
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            
            // Показать правильный ответ, если ответ проверен и неверен
            if (isAnswerChecked && !isCorrect) {
                Text(
                    text = "Правильный ответ: ${question.correctAnswer}",
                    color = Color(0xFF4CAF50),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
} 