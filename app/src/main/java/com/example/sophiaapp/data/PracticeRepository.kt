package com.example.sophiaapp.data

import com.example.sophiaapp.domain.models.Quiz
import com.example.sophiaapp.domain.models.QuizQuestion
import com.example.sophiaapp.domain.models.*

object PracticeRepository {

    fun getPracticeForCourse(courseId: String, courseTitle: String): PracticeContent {
        return when(courseId) {
            "1" -> QuizContent(createDialecticsQuiz()) // Для первого курса - тест
            "2" -> PlaceholderContent("Практическое задание по философии древнего мира находится в разработке") // Заглушка
            "3" -> GameContent(  // Заменить PlaceholderContent на GameContent
                gameType = PracticeType.GAME_1,
                gameTitle = "Соединение типов связей",
                gameDescription = "Соедините утверждения с соответствующими типами связей",
                gameData = "connections_types" // ID игры, который будет передан в MatchingGameScreen
            )
            "4" -> PlaceholderContent("Практическое задание  находится в разработке") // Заглушка// Заглушка
            "5" -> PlaceholderContent("Практическое задание  находится в разработке")
            "6" -> PlaceholderContent("Практическое задание  находится в разработке")
            "7" -> PlaceholderContent("Практическое задание  находится в разработке")
            "8" -> PlaceholderContent("Практическое задание  находится в разработке")
            else -> PlaceholderContent("Практическое задание для этого курса скоро будет доступно")
        }

        // Когда будут готовы игры, вы можете заменить заглушки на игры:
        // "2" -> GameContent(
        //     gameType = PracticeType.GAME_1,
        //     gameTitle = "Философский пазл",
        //     gameDescription = "Соберите пазл, соединяя философские школы с их создателями"
        // )
    }

    private fun createDialecticsQuiz(): Quiz {
        return Quiz(
            title = "Тест по диалектике",
            questions = listOf(
                QuizQuestion(
                    question = "Что является исходным принципом развития всего существующего?",
                    options = listOf("Метафизика", "Диалектика", "Онтология", "Гносеология"),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    question = "Развитие - это:",
                    options = listOf(
                        "Обратимое изменение объектов",
                        "Необратимое, закономерное и направленное изменение объектов",
                        "Хаотичное изменение объектов",
                        "Циклическое повторение процессов"
                    ),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    question = "Какие элементы входят в структуру диалектики?",
                    options = listOf(
                        "Принципы, законы и категории диалектики",
                        "Только законы диалектики",
                        "Принципы и методы",
                        "Категории и понятия"
                    ),
                    correctAnswerIndex = 0
                ),
                QuizQuestion(
                    question = "Движение в диалектике характеризуется:",
                    options = listOf(
                        "Прерывностью",
                        "Целостностью, непрерывностью и наличием противоречий",
                        "Отсутствием противоречий",
                        "Обратимостью"
                    ),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    question = "Преобразованный в ходе движения объект:",
                    options = listOf(
                        "Полностью теряет свои прежние черты",
                        "Остается неизменным",
                        "Сохраняет черты предшественника и приобретает новые",
                        "Возвращается к исходному состоянию"
                    ),
                    correctAnswerIndex = 2
                )
            )
        )
    }


}