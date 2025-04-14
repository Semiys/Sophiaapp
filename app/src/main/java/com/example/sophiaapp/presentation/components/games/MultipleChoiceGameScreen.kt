package com.example.sophiaapp.presentation.components.games

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.sophiaapp.domain.models.MultipleChoiceQuestion
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.viewmodels.MultipleChoiceGameViewModel
import com.example.sophiaapp.utils.localization.AppStrings
import androidx.compose.ui.platform.LocalContext
import com.example.sophiaapp.data.local.database.AppDatabase
import com.example.sophiaapp.data.repository.FirebaseAuthRepository
import com.example.sophiaapp.data.repository.UserProgressRepository
import com.example.sophiaapp.domain.models.LocationType
import com.example.sophiaapp.presentation.viewmodels.UserProgressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleChoiceGameScreen(
    gameId: String,
    navController: NavHostController,
    courseId: String,
    onGameCompleted: (Int, Int) -> Unit,
    viewModel: MultipleChoiceGameViewModel = viewModel(factory = MultipleChoiceGameViewModel.Factory(gameId)),
    // Добавляем progressViewModel
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
    val game by viewModel.game.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val answersChecked by viewModel.answersChecked.collectAsState()
    val answerResults by viewModel.answerResults.collectAsState()
    val score by viewModel.score.collectAsState()
    val selectedOptions by viewModel.selectedOptions.collectAsState()
    val allAnswersSelected by viewModel.allAnswersSelected.collectAsState()

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
                MultipleChoiceQuestionItem(
                    question = question,
                    selectedOptionIndex = selectedOptions[question.id] ?: -1,
                    isAnswerChecked = answersChecked,
                    isCorrect = answerResults[question.id] ?: false,
                    onOptionSelected = { optionIndex ->
                        if (!answersChecked) {
                            viewModel.selectAnswer(question.id, optionIndex)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
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
                
                // Кнопка "Проверить" активна только если выбраны все ответы и еще не проверены
                val checkButtonEnabled = allAnswersSelected && !answersChecked

                CustomButton(
                    onClick = {
                        if (checkButtonEnabled) {
                            val correctCount = viewModel.checkAnswers() // Получаем непосредственный результат
                            onGameCompleted(correctCount, currentGame.questions.size)
                        }
                    },
                    text = "Проверить",
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    backgroundRes = if (checkButtonEnabled)
                        R.drawable.card_background
                    else
                        R.drawable.continue_button,
                    textColor = if (checkButtonEnabled) Color.Black else Color.Gray
                )
            }
            
            // Подсказка, если не все ответы выбраны
            if (!allAnswersSelected && !answersChecked) {
                Text(
                    text = "Пожалуйста, выберите ответы на все вопросы",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            
            // Результаты и кнопка возврата
            AnimatedVisibility(
                visible = answersChecked,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
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
                                // Добавляем обновление местоположения перед навигацией
                                progressViewModel.updateLastLocation(
                                    locationType = LocationType.PRACTICE,
                                    courseId = courseId,
                                    sectionId = ""
                                )

                                // onGameCompleted уже вызвана при проверке, не нужно вызывать повторно
                                // onGameCompleted(score, currentGame.questions.size) - удалите эту строку

                                navController.navigate(Screen.Library.route) {
                                    popUpTo(Screen.Library.route) {
                                        inclusive = false
                                    }
                                }
                            },
                            text = AppStrings.Course.BACK_TO_COURSE,
                            backgroundRes = R.drawable.card_background,
                            textColor = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceQuestionItem(
    question: MultipleChoiceQuestion,
    selectedOptionIndex: Int,
    isAnswerChecked: Boolean,
    isCorrect: Boolean,
    onOptionSelected: (Int) -> Unit
) {
    val borderColor = when {
        !isAnswerChecked -> if (selectedOptionIndex >= 0) Color(0xFF1E88E5) else Color(0xFFF44336) // Подсветка красным если не выбран ответ
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
            // Разделяем текст вопроса по маркеру [BLANK]
            val parts = question.text.split("[BLANK]")

            // Текст до пропуска
            if (parts.isNotEmpty() && parts[0].isNotEmpty()) {
                Text(
                    text = parts[0],
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            // Создаем строку с выбранным вариантом или пропуском
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                if (selectedOptionIndex >= 0 && selectedOptionIndex < question.options.size) {
                    Text(
                        text = question.options[selectedOptionIndex],
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            !isAnswerChecked -> MaterialTheme.colorScheme.primary
                            isCorrect -> Color(0xFF4CAF50)
                            else -> Color(0xFFE57373)
                        },
                        modifier = Modifier
                            .background(
                                color = when {
                                    !isAnswerChecked -> Color(0x1F1E88E5)
                                    isCorrect -> Color(0x1F4CAF50)
                                    else -> Color(0x1FE57373)
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                } else {
                    Text(
                        text = "_______",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            
            // Текст после пропуска
            if (parts.size > 1 && parts[1].isNotEmpty()) {
                Text(
                    text = parts[1],
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Варианты ответов
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                question.options.forEachIndexed { index, option ->
                    OptionItem(
                        option = option,
                        isSelected = index == selectedOptionIndex,
                        isAnswerChecked = isAnswerChecked,
                        isCorrect = isAnswerChecked && index == question.correctAnswerIndex,
                        onOptionSelected = { onOptionSelected(index) }
                    )
                }
            }
            
            // Показать правильный ответ, если ответ проверен и неверен
            if (isAnswerChecked && !isCorrect) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Правильный ответ: ")
                        withStyle(style = SpanStyle(
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )) {
                            append(question.options[question.correctAnswerIndex])
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun OptionItem(
    option: String,
    isSelected: Boolean,
    isAnswerChecked: Boolean,
    isCorrect: Boolean,
    onOptionSelected: () -> Unit
) {
    val backgroundColor = when {
        isSelected && isAnswerChecked && isCorrect -> Color(0x1F4CAF50) // Светло-зеленый
        isSelected && isAnswerChecked && !isCorrect -> Color(0x1FE57373) // Светло-красный
        isSelected -> Color(0x1F1E88E5) // Светло-синий
        else -> Color.Transparent
    }
    
    val textColor = when {
        isSelected && isAnswerChecked && isCorrect -> Color(0xFF4CAF50) // Зеленый
        isSelected && isAnswerChecked && !isCorrect -> Color(0xFFE57373) // Красный
        isSelected -> MaterialTheme.colorScheme.primary // Стандартный цвет акцента
        else -> MaterialTheme.colorScheme.onSurface // Стандартный цвет текста
    }
    
    val borderColor = when {
        isSelected && isAnswerChecked && isCorrect -> Color(0xFF4CAF50) // Зеленый
        isSelected && isAnswerChecked && !isCorrect -> Color(0xFFE57373) // Красный
        isSelected -> MaterialTheme.colorScheme.primary // Стандартный цвет акцента
        else -> Color.LightGray // Светло-серый для неактивных
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = !isAnswerChecked) { onOptionSelected() }
            .background(backgroundColor)
            .padding(12.dp),
    ) {
        Text(
            text = option,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            modifier = Modifier.background(Color.Transparent)
        )
    }
} 