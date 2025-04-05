package com.example.sophiaapp.presentation.components.games

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sophiaapp.R
import com.example.sophiaapp.domain.models.MatchingItem
import com.example.sophiaapp.domain.models.MatchingItemType
import com.example.sophiaapp.domain.models.MatchingPair
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.viewmodels.MatchingGameViewModel
import com.example.sophiaapp.utils.localization.AppStrings

@Composable
fun MatchingGameScreen(
    gameId: String,
    onGameCompleted: (Int, Int) -> Unit,
    navController: NavHostController? = null,
    courseId: String? = null,
    viewModel: MatchingGameViewModel = viewModel(factory = MatchingGameViewModel.Factory(gameId))
) {
    val game by viewModel.game.collectAsState()
    val userPairs by viewModel.userPairs.collectAsState()
    val isGameCompleted by viewModel.isGameCompleted.collectAsState()
    val showResults by viewModel.showResults.collectAsState()
    val score by viewModel.score.collectAsState()
    val scrollState = rememberScrollState()

    var selectedQuestionId by remember { mutableStateOf<String?>(null) }

    // Цвета для соединительных линий
    val lineColors = remember {
        listOf(
            Color(0xFF4CAF50), // Зеленый
            Color(0xFF2196F3), // Синий
            Color(0xFFFFC107), // Желтый
            Color(0xFFE91E63)  // Розовый
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Заголовок игры
        Text(
            text = "Типы связей",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Описание игры
        game?.let { currentGame ->
            Text(
                text = currentGame.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Список вопросов (текст) вверху
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                currentGame.questions.forEachIndexed { index, question ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = (index + 1).toString(),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = question.text,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Список ответов (текст) вверху
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                currentGame.answers.forEachIndexed { index, answer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = ('a' + index).toString(),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = answer.text,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Область игры с номерами и буквами без текста
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Левая колонка с номерами
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(end = 16.dp)
                ) {
                    currentGame.questions.forEachIndexed { index, question ->
                        // Проверка, соединен ли этот вопрос
                        val isConnected = userPairs.any { it.questionId == question.id }
                        NumberItem(
                            number = index + 1,
                            isSelected = selectedQuestionId == question.id,
                            isConnected = isConnected,
                            questionId = question.id,
                            isInteractionLocked = showResults,
                            onSelected = {
                                // Если результаты уже показаны, нельзя менять выбор
                                if (!showResults) {
                                    selectedQuestionId = if (selectedQuestionId == question.id) null else question.id
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Правая колонка с буквами
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    currentGame.answers.forEachIndexed { index, answer ->
                        // Проверка, соединен ли этот ответ
                        val isConnected = userPairs.any { it.answerId == answer.id }
                        LetterItem(
                            letter = ('a' + index).toString(),
                            answerId = answer.id,
                            isConnected = isConnected,
                            isInteractionLocked = showResults,
                            onSelected = {
                                // Если результаты уже показаны, нельзя создавать соединения
                                if (!showResults) {
                                    // Если выбран вопрос, создаем соединение
                                    selectedQuestionId?.let { questionId ->
                                        viewModel.createConnection(questionId, answer.id)
                                        selectedQuestionId = null // Сбрасываем выбор
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Соединительные линии
                Canvas(modifier = Modifier.fillMaxSize()) {
                    userPairs.forEachIndexed { index, pair ->
                        // Находим позиции вопроса и ответа
                        val questionIndex = currentGame.questions.indexOfFirst { it.id == pair.questionId }
                        val answerIndex = currentGame.answers.indexOfFirst { it.id == pair.answerId }

                        if (questionIndex >= 0 && answerIndex >= 0) {
                            // Вычисляем позиции для линии
                            val startY = questionIndex * (48.dp.toPx() + 16.dp.toPx()) + 24.dp.toPx()
                            val endY = answerIndex * (48.dp.toPx() + 16.dp.toPx()) + 24.dp.toPx()

                            // Рисуем линию с цветом, соответствующим индексу пары
                            drawLine(
                                color = lineColors[index % lineColors.size],
                                start = Offset(size.width * 0.2f, startY),
                                end = Offset(size.width * 0.8f, endY),
                                strokeWidth = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }

            // Кнопки управления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.resetGame() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Сбросить")
                }

                Button(
                    onClick = {
                        viewModel.checkAnswers()
                        onGameCompleted(score, currentGame.questions.size)
                    },
                    enabled = isGameCompleted && !showResults // Активна, только если все соединения созданы и результаты еще не показаны
                ) {
                    Text("Проверить")
                }
            }

            // Результаты (показываются только если пользователь нажал кнопку "Проверить")
            if (showResults) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Результат: $score из ${currentGame.questions.size}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        if (score == currentGame.questions.size) {
                            Text(
                                text = "Отлично! Все соединения верны!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(
                                text = "Попробуйте еще раз для лучшего результата!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Нажмите кнопку «Сбросить», чтобы изменить ответы",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Кнопка возврата к курсу после результатов
                if (navController != null && courseId != null) {
                    CustomButton(
                        text = AppStrings.Quiz.RETURN_TO_COURSE,
                        onClick = {
                            navController.navigate(Screen.Library.route) {
                                popUpTo(Screen.Library.route) {
                                    inclusive = false
                                }
                            }
                        },
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

@Composable
fun NumberItem(
    number: Int,
    isSelected: Boolean,
    isConnected: Boolean,
    questionId: String,
    isInteractionLocked: Boolean,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isConnected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isInteractionLocked, onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = if (isInteractionLocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LetterItem(
    letter: String,
    answerId: String,
    isConnected: Boolean,
    isInteractionLocked: Boolean,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = if (isConnected) 
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                else 
                    MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isInteractionLocked, onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.titleMedium,
            color = if (isInteractionLocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else MaterialTheme.colorScheme.onSurface
        )
    }
}