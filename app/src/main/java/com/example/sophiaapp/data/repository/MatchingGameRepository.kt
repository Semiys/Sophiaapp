package com.example.sophiaapp.data.repository

import com.example.sophiaapp.domain.models.MatchingGame
import com.example.sophiaapp.domain.models.MatchingItem
import com.example.sophiaapp.domain.models.MatchingItemType
import com.example.sophiaapp.domain.models.MatchingPair

class MatchingGameRepository {

    fun getMatchingGameById(id: String): MatchingGame {
        // В будущем здесь может быть получение из Firebase или другого источника
        return when (id) {
            "connections_types" -> createConnectionTypesGame()
            // Другие игры могут быть добавлены здесь
            else -> throw IllegalArgumentException("Unknown game ID: $id")
        }
    }

    private fun createConnectionTypesGame(): MatchingGame {
        val questions = listOf(
            MatchingItem("q1", "Повышение температуры воздуха привело к таянию снега.", MatchingItemType.QUESTION),
            MatchingItem("q2", "Экономическая система государства включает производство, распределение и потребление.", MatchingItemType.QUESTION),
            MatchingItem("q3", "Развитие человечества от первобытного общества до современной цивилизации.", MatchingItemType.QUESTION),
            MatchingItem("q4", "Загрязнение воздуха в городе влияет на здоровье его жителей, состояние растений и чистоту воды.", MatchingItemType.QUESTION)
        )

        val answers = listOf(
            MatchingItem("a1", "системная связь", MatchingItemType.ANSWER),
            MatchingItem("a2", "причинно-следственная связь", MatchingItemType.ANSWER),
            MatchingItem("a3", "всеобщая связь", MatchingItemType.ANSWER),
            MatchingItem("a4", "историческая связь", MatchingItemType.ANSWER)
        )

        val correctPairs = listOf(
            MatchingPair("q1", "a2", true),
            MatchingPair("q2", "a1", true),
            MatchingPair("q3", "a4", true),
            MatchingPair("q4", "a3", true)
        )

        return MatchingGame(
            "connections_types",
            "Типы связей",
            "Соедините утверждение с соответствующим типом связи",
            questions,
            answers,
            correctPairs
        )
    }
}