package com.example.sophiaapp.presentation.components.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sophiaapp.R
import com.example.sophiaapp.domain.models.FillInBlankQuestion
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.presentation.common.components.CustomButton
import com.example.sophiaapp.presentation.viewmodels.FillInBlankGameViewModel
import com.example.sophiaapp.utils.localization.AppStrings

@Composable
fun FillInBlankGameScreen(
    gameId: String,
    onGameCompleted: (Int, Int) -> Unit,
    navController: NavHostController? = null,
    courseId: String? = null,
    viewModel: FillInBlankGameViewModel = viewModel(factory = FillInBlankGameViewModel.Factory(gameId))
) {
    val game by viewModel.game.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val answersChecked by viewModel.answersChecked.collectAsState()
    val answerResults by viewModel.answerResults.collectAsState()
    val score by viewModel.score.collectAsState()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    // Текущие значения в текстовых полях
    val textFieldValues = remember { mutableStateMapOf<String, TextFieldValue>() }
    
    // Инициализация текстовых полей
    LaunchedEffect(userAnswers) {
        userAnswers.forEach { (questionId, answer) ->
            textFieldValues[questionId] = TextFieldValue(
                text = answer,
                selection = TextRange(answer.length)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Заголовок игры
        game?.let { currentGame ->
            Text(
                text = currentGame.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Описание игры
            Text(
                text = currentGame.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Вопросы с полями для ввода
            currentGame.questions.forEach { question ->
                FillInBlankQuestionItem(
                    question = question,
                    textFieldValue = textFieldValues[question.id] ?: TextFieldValue(""),
                    isChecked = answersChecked,
                    isCorrect = answerResults[question.id] ?: false,
                    onValueChange = { newValue ->
                        textFieldValues[question.id] = newValue
                        viewModel.updateAnswer(question.id, newValue.text)
                    },
                    onDone = { focusManager.clearFocus() }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Кнопки управления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { 
                        viewModel.resetGame()
                        focusManager.clearFocus()
                    },
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
                        focusManager.clearFocus()
                    },
                    enabled = !answersChecked // Активна, только если ответы еще не проверены
                ) {
                    Text("Проверить")
                }
            }

            // Результаты (показываются только если пользователь нажал кнопку "Проверить")
            if (answersChecked) {
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
                                text = "Отлично! Все ответы верны!",
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
fun FillInBlankQuestionItem(
    question: FillInBlankQuestion,
    textFieldValue: TextFieldValue,
    isChecked: Boolean,
    isCorrect: Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    onDone: () -> Unit
) {
    val parts = question.text.split("____")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Текст с пропуском
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Первая часть текста
                if (parts.isNotEmpty() && parts[0].isNotEmpty()) {
                    Text(
                        text = parts[0],
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                // Текстовое поле для ввода ответа
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { if (!isChecked) onValueChange(it) },
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .defaultMinSize(minWidth = 100.dp),
                    enabled = !isChecked,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledBorderColor = if (isChecked && !isCorrect) 
                            Color.Red 
                        else 
                            MaterialTheme.colorScheme.outline,
                        disabledTextColor = if (isChecked && !isCorrect) 
                            Color.Red 
                        else 
                            MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onDone() }
                    ),
                    singleLine = true
                )
                
                // Вторая часть текста
                if (parts.size > 1 && parts[1].isNotEmpty()) {
                    Text(
                        text = parts[1],
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            
            // Показываем правильный ответ, если ответ неверный и проверка выполнена
            if (isChecked && !isCorrect) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Правильный ответ: ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append(question.correctAnswer)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 