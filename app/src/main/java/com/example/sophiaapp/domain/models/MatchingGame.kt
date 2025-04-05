package com.example.sophiaapp.domain.models

data class MatchingItem(
    val id: String,  // Уникальный идентификатор
    val text: String, // Текст вопроса или ответа
    val type: MatchingItemType // Тип элемента (вопрос или ответ)
)

enum class MatchingItemType {
    QUESTION, ANSWER
}

data class MatchingPair(
    val questionId: String,
    val answerId: String,
    val isCorrect: Boolean = false // Указывает, правильная ли это пара
)

data class MatchingGame(
    val id: String,
    val title: String,
    val description: String,
    val questions: List<MatchingItem>,
    val answers: List<MatchingItem>,
    val correctPairs: List<MatchingPair> // Правильные пары для проверки
)