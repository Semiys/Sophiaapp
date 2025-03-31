package com.example.sophiaapp.domain.models

// Базовый интерфейс для всех типов практического контента
interface PracticeContent {
    val type: PracticeType
}

// Тип практического контента
enum class PracticeType {
    QUIZ,          // Тест с вопросами
    PLACEHOLDER,   // Заглушка "Скоро будет доступно"
    GAME_1,        // Первая игра
    GAME_2,        // Вторая игра
    GAME_3,        // Третья игра
    GAME_4         // Четвёртая игра
}

// Контент-тест
data class QuizContent(
    val quiz: Quiz
) : PracticeContent {
    override val type = PracticeType.QUIZ
}

// Контент-заглушка
data class PlaceholderContent(
    val message: String
) : PracticeContent {
    override val type = PracticeType.PLACEHOLDER
}

// Пример модели для игры (вы можете адаптировать её под свои нужды)
data class GameContent(
    val gameType: PracticeType,
    val gameTitle: String,
    val gameDescription: String,
    val gameData: String = "" // Любые дополнительные данные для игры
) : PracticeContent {
    override val type = gameType
}