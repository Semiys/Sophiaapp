package com.example.sophiaapp.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sophiaapp.R
import com.example.sophiaapp.domain.models.QuizQuestion


@Composable
fun QuizQuestionCard(
    question: QuizQuestion,
    questionNumber: Int,
    selectedAnswerIndex: Int?,
    onAnswerSelected: (Int) -> Unit,
    showCorrectAnswer: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.question),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = "$questionNumber. ${question.question}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding( 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEachIndexed { index, option ->
            val isSelected = selectedAnswerIndex == index
            val isCorrect = index == question.correctAnswerIndex

            val backgroundRes = when {
                showCorrectAnswer && isCorrect -> R.drawable.answer_true
                showCorrectAnswer && isSelected && !isCorrect -> R.drawable.answer_false
                isSelected -> R.drawable.answer_choice
                else -> R.drawable.answer
            }
            Box(
                modifier=Modifier
                    .fillMaxWidth()
                    .padding(vertical=8.dp)
                    .clickable(onClick={onAnswerSelected(index)})
            ) {
                Image(
                    painter = painterResource(id = backgroundRes),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onAnswerSelected(index) }

                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

        }

    }
}