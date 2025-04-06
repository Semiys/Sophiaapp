package com.example.sophiaapp.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.R
import com.example.sophiaapp.domain.models.Quiz
import com.example.sophiaapp.utils.localization.AppStrings

@Composable
fun QuizScreen(
    quiz:Quiz,
    onQuizComplete:(Int,Int)->Unit,
    onReturnToCourse:() -> Unit,
    modifier:Modifier=Modifier
){
    val scrollState=rememberScrollState()
    var currentQuestionIndex by remember {mutableStateOf(0)}
    var score by remember {mutableStateOf(0)}
    var selectedAnswers by remember{mutableStateOf(List(quiz.questions.size){-1})}
    var quizCompleted by remember{mutableStateOf(false)}
    
    // Проверка, все ли вопросы отвечены
    val allQuestionsAnswered = selectedAnswers.none { it == -1 }


    val currentQuestion=quiz.questions[currentQuestionIndex]

    Column(
        modifier=modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.card_background_not),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = quiz.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        QuizQuestionCard(
            question=currentQuestion,
            questionNumber=currentQuestionIndex+1,
            selectedAnswerIndex=selectedAnswers[currentQuestionIndex],
            onAnswerSelected={answerIndex->
                if (!quizCompleted){
                    val newSelectedAnswers=selectedAnswers.toMutableList()
                    newSelectedAnswers[currentQuestionIndex]=answerIndex
                    selectedAnswers=newSelectedAnswers
                }
            },
            showCorrectAnswer=quizCompleted,
            modifier=Modifier.padding(bottom=16.dp)
        )
        Row(
            horizontalArrangement=Arrangement.SpaceBetween,
            modifier=Modifier.fillMaxWidth()
        ){
            if (currentQuestionIndex > 0){
                CustomButton(
                    text="Предыдущий",
                    onClick={
                        currentQuestionIndex--
                    },
                    backgroundRes = R.drawable.card_background,
                    textColor=MaterialTheme.colorScheme.primary,
                    modifier=Modifier.weight(1f).padding(end=8.dp)
                )
            } else{
                Spacer(modifier=Modifier.weight(1f))
            }
            if (currentQuestionIndex < quiz.questions.size -1){
                CustomButton(
                    text="Следующий",
                    onClick={
                        currentQuestionIndex++
                    },
                    backgroundRes = R.drawable.card_background,
                    textColor=MaterialTheme.colorScheme.primary,
                    modifier=Modifier.weight(1f).padding(start=8.dp)
                )
            } else if (!quizCompleted){
                // Определяем фон и цвет кнопки в зависимости от того, все ли вопросы отвечены
                val backgroundRes = if (allQuestionsAnswered) R.drawable.card_background else R.drawable.card_background
                val textColor = if (allQuestionsAnswered) Color.Black else Color.Gray
                
                CustomButton(
                    text="Завершить тест",
                    onClick={
                        if (allQuestionsAnswered) {
                            quizCompleted=true
                            score=selectedAnswers.filterIndexed{index,answer ->
                                answer==quiz.questions[index].correctAnswerIndex
                            }.size
                            onQuizComplete(score,quiz.questions.size)
                        }
                    },
                    backgroundRes = backgroundRes,
                    textColor = textColor,
                    modifier=Modifier.weight(1f).padding(start=8.dp)
                )
            }
        }
        
        // Показываем предупреждение, если не на все вопросы даны ответы
        if (!quizCompleted && !allQuestionsAnswered && currentQuestionIndex == quiz.questions.size - 1) {
            Text(
                text = "Пожалуйста, ответьте на все вопросы перед завершением теста",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
        
        if (quizCompleted) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.card_background_not),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = String.format(AppStrings.Quiz.RESULTS, score.toString(), quiz.questions.size.toString()),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            CustomButton(
                text=AppStrings.Quiz.RETURN_TO_COURSE,
                onClick= onReturnToCourse,
                backgroundRes=R.drawable.card_background,
                textColor=MaterialTheme.colorScheme.primary,
                modifier=Modifier.fillMaxWidth().padding(top=16.dp)
            )
        }
    }
}
