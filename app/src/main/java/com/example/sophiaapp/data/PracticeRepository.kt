package com.example.sophiaapp.data

import com.example.sophiaapp.domain.models.Quiz
import com.example.sophiaapp.domain.models.QuizQuestion
import com.example.sophiaapp.domain.models.*

object PracticeRepository {

    fun getPracticeForCourse(courseId: String, courseTitle: String): PracticeContent {
        return when(courseId) {
            "1" -> QuizContent(createDialecticsQuiz()) // Для первого курса - тест
            "2" -> GameContent(  // Игра-вписывалка (FillInBlankGame)
                gameType = PracticeType.GAME_2,
                gameTitle = "Диалектика в разные эпохи",
                gameDescription = "Заполните пропуски в предложениях, вписав правильные слова",
                gameData = "ancient_philosophy" // ID игры, который будет передан в FillInBlankGameScreen
            )
            "3" -> GameContent(  // Соединение типов связей
                gameType = PracticeType.GAME_1,
                gameTitle = "Соединение типов связей",
                gameDescription = "Соедините утверждения с соответствующими типами связей",
                gameData = "connections_types" // ID игры, который будет передан в MatchingGameScreen
            )
            "4" -> GameContent(  // Игра "Вставлялка" - варианты ответов
                gameType = PracticeType.GAME_3,
                gameTitle = "Законы в науке",
                gameDescription = "Выберите правильный вариант, чтобы заполнить пропуск в тексте",
                gameData = "science_laws" // ID игры, который будет передан в MultipleChoiceGameScreen
            )
            "5" -> GameContent(  // Соединение типов связей
                gameType = PracticeType.GAME_1,
                gameTitle = "Противоположности в системах",
                gameDescription = "Соотнесите системы с их противоположностями",
                gameData = "opposites_systems" // ID новой игры
            )
            "6" -> GameContent(  // Игра "Вставлялка" - варианты ответов
                gameType = PracticeType.GAME_3,
                gameTitle = "Количественные и качественные изменения",
                gameDescription = "Выберите правильный вариант для заполнения пропуска",
                gameData = "quality_quantity" // ID новой игры
            )
            "7" -> GameContent(  // Игра-вписывалка (FillInBlankGame)
                gameType = PracticeType.GAME_2,
                gameTitle = "Закон отрицания отрицания",
                gameDescription = "Заполните пропуски в предложениях, вписав правильные слова",
                gameData = "negation_law"
            )
            "8" -> QuizContent(createCategoriesQuiz()) // Новый тест для курса 8
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

    private fun createCategoriesQuiz(): Quiz {
        return Quiz(
            title = "Тест по категориям диалектики",
            questions = listOf(
                QuizQuestion(
                    question = "Что такое единичное в диалектике?",
                    options = listOf(
                        "Общие черты всех предметов",
                        "Сходство между явлениями",
                        "Отдельный предмет или явление, отличающийся своими свойствами от других",
                        "Совокупность всех предметов"
                    ),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    question = "Как соотносятся явление и сущность?",
                    options = listOf(
                        "Они полностью совпадают",
                        "Они диалектически связаны, но не совпадают",
                        "Они никак не связаны между собой",
                        "Они являются одним и тем же"
                    ),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    question = "Что такое содержание согласно тексту?",
                    options = listOf(
                        "Совокупность элементов и их взаимодействий",
                        "Внешний вид предмета",
                        "Способ существования предмета",
                        "Структура явления"
                    ),
                    correctAnswerIndex = 0
                ),
                QuizQuestion(
                    question = "Как соотносятся часть и целое в диалектике?",
                    options = listOf(
                        "Целое равно сумме частей",
                        "Целое больше суммы его частей",
                        "Целое меньше суммы частей",
                        "Части не связаны с целым"
                    ),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    question = "Что такое детерминизм?",
                    options = listOf(
                        "Концепция закономерной причинной обусловленности явлений",
                        "Теория случайных событий",
                        "Учение о форме предметов",
                        "Концепция хаотичности мира"
                    ),
                    correctAnswerIndex = 0
                ),
                QuizQuestion(
                    question = "Что является примером взаимодействия причины и следствия?",
                    options = listOf(
                        "Падение домино",
                        "Обратное воздействие следствия на свою причину",
                        "Случайное событие",
                        "Необходимое событие"
                    ),
                    correctAnswerIndex = 1
                ),
                QuizQuestion(
                    question = "Как соотносятся необходимость и случайность?",
                    options = listOf(
                        "Они противоположны и не связаны",
                        "Они полностью совпадают",
                        "Случайность является формой проявления необходимости",
                        "Необходимость исключает случайность"
                    ),
                    correctAnswerIndex = 2
                ),
                QuizQuestion(
                    question = "Что такое повод в причинно-следственных связях?",
                    options = listOf(
                        "Явление, дающее толчок к следствию, но не вызывающее его напрямую",
                        "Главная причина события",
                        "Условие события",
                        "Само следствие"
                    ),
                    correctAnswerIndex = 0
                ),
                QuizQuestion(
                    question = "Как связаны возможность и действительность?",
                    options = listOf(
                        "Возможность при определенных условиях превращается в действительность",
                        "Они никак не связаны",
                        "Они всегда совпадают",
                        "Действительность исключает возможность"
                    ),
                    correctAnswerIndex = 0
                ),
                QuizQuestion(
                    question = "Что характеризует категорию \"общее\"?",
                    options = listOf(
                        "Уникальные черты предмета",
                        "Случайные признаки",
                        "Сходство и однотипность предметов в некоторых отношениях",
                        "Внешние признаки явления"
                    ),
                    correctAnswerIndex = 2
                )
            )
        )
    }


}