package com.example.sophiaapp.data.repository

import com.example.sophiaapp.domain.models.FillInBlankGame
import com.example.sophiaapp.domain.models.FillInBlankQuestion

class FillInBlankGameRepository {

    fun getFillInBlankGameById(id: String): FillInBlankGame {
        // В будущем здесь может быть получение из Firebase или другого источника
        return when (id) {
            "ancient_philosophy" -> createAncientPhilosophyGame()
            "negation_law" -> createNegationLawGame() // Добавляем новую игру
            // Другие игры могут быть добавлены здесь
            else -> throw IllegalArgumentException("Unknown game ID: $id")
        }
    }

    private fun createAncientPhilosophyGame(): FillInBlankGame {
        return FillInBlankGame(
            id = "ancient_philosophy",
            title = "Диалектика в разные эпохи",
            description = "Заполните пропуски в предложениях, вписав правильные слова",
            questions = listOf(
                FillInBlankQuestion(
                    id = "q1",
                    text = "В ____ диалектика фокусировалась на всеобщей изменчивости и взаимосвязи явлений.",
                    correctAnswer = "античности"
                ),
                FillInBlankQuestion(
                    id = "q2",
                    text = "____ разработал трехступенчатую модель познания: от чувственного восприятия через рассудок к разуму.",
                    correctAnswer = "Гегель"
                ),
                FillInBlankQuestion(
                    id = "q3",
                    text = "В Средневековье ____ применял диалектику для обоснования религиозных догматов.",
                    correctAnswer = "Фома Аквинский"
                )
            )
        )
    }

    private fun createNegationLawGame(): FillInBlankGame {
        return FillInBlankGame(
            id = "negation_law",
            title = "Закон отрицания отрицания",
            description = "Заполните пропуски в предложениях, вписав правильные слова",
            questions = listOf(
                FillInBlankQuestion(
                    id = "q1",
                    text = "Развитие происходит по ____, где каждый новый этап как бы повторяет предыдущий, но на более высоком уровне.",
                    correctAnswer = "спирали"
                ),
                FillInBlankQuestion(
                    id = "q2",
                    text = "В процессе развития новое, возникая из ____, впитывает в себя все положительное, что было в нем.",
                    correctAnswer = "старого"
                ),
                FillInBlankQuestion(
                    id = "q3",
                    text = "Этот закон показывает ____ развития от низшего к высшему.",
                    correctAnswer = "направление"
                )
            )
        )
    }
} 