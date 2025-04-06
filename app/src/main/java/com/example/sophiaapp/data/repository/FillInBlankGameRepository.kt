package com.example.sophiaapp.data.repository

import com.example.sophiaapp.domain.models.FillInBlankGame
import com.example.sophiaapp.domain.models.FillInBlankQuestion

class FillInBlankGameRepository {

    fun getFillInBlankGameById(id: String): FillInBlankGame {
        // В будущем здесь может быть получение из Firebase или другого источника
        return when (id) {
            "ancient_philosophy" -> createAncientPhilosophyGame()
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
} 