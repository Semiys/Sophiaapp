package com.example.sophiaapp.data.repository

import com.example.sophiaapp.domain.models.MultipleChoiceGame
import com.example.sophiaapp.domain.models.MultipleChoiceQuestion

class MultipleChoiceGameRepository {

    fun getMultipleChoiceGameById(id: String): MultipleChoiceGame {
        // В будущем здесь может быть получение из Firebase или другого источника
        return when (id) {
            "science_laws" -> createScienceLawsGame()
            // Другие игры можно добавить здесь
            else -> throw IllegalArgumentException("Unknown game ID: $id")
        }
    }

    private fun createScienceLawsGame(): MultipleChoiceGame {
        return MultipleChoiceGame(
            id = "science_laws",
            title = "Законы в науке",
            description = "Выберите правильный вариант для заполнения пропуска",
            questions = listOf(
                MultipleChoiceQuestion(
                    id = "q1",
                    text = "В науке [BLANK] определяется как объективная, общая, стабильная и необходимая связь между явлениями.",
                    options = listOf("закон", "теория", "гипотеза"),
                    correctAnswerIndex = 0 // "закон"
                ),
                MultipleChoiceQuestion(
                    id = "q2",
                    text = "Закон Архимеда является примером [BLANK] закона по сфере применимости, так как относится к конкретной области науки.",
                    options = listOf("всеобщего", "частнонаучного", "общенаучного"),
                    correctAnswerIndex = 1 // "частнонаучного"
                ),
                MultipleChoiceQuestion(
                    id = "q3",
                    text = "Связь между телом и жидкостью в законе Архимеда является [BLANK], так как не зависит от воли человека.",
                    options = listOf("субъективной", "теоретической", "объективной"),
                    correctAnswerIndex = 2 // "объективной"
                ),
                MultipleChoiceQuestion(
                    id = "q4",
                    text = "По форме проявления законы могут быть [BLANK] и статистическими.",
                    options = listOf("динамическими", "структурными", "функциональными"),
                    correctAnswerIndex = 0 // "динамическими"
                )
            )
        )
    }
} 