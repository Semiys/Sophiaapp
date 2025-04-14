package com.example.sophiaapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sophiaapp.data.repository.MultipleChoiceGameRepository
import com.example.sophiaapp.domain.models.MultipleChoiceGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MultipleChoiceGameViewModel(
    private val repository: MultipleChoiceGameRepository,
    private val gameId: String
) : ViewModel() {

    private val _game = MutableStateFlow<MultipleChoiceGame?>(null)
    val game: StateFlow<MultipleChoiceGame?> = _game

    // Ответы пользователя: ID вопроса -> выбранный вариант (индекс)
    private val _userAnswers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val userAnswers: StateFlow<Map<String, Int>> = _userAnswers

    // Флаг, показывающий, проверены ли ответы
    private val _answersChecked = MutableStateFlow(false)
    val answersChecked: StateFlow<Boolean> = _answersChecked

    // Результаты проверки: ID вопроса -> правильно/неправильно
    private val _answerResults = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val answerResults: StateFlow<Map<String, Boolean>> = _answerResults

    // Общий результат - количество правильных ответов
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    // Выбранные варианты: ID вопроса -> индекс выбранного варианта
    // Изменяем логику - теперь храним только текущий выбранный вариант для каждого вопроса
    private val _selectedOptions = MutableStateFlow<Map<String, Int>>(emptyMap())
    val selectedOptions: StateFlow<Map<String, Int>> = _selectedOptions
    
    // Флаг, показывающий, заполнены ли все ответы
    private val _allAnswersSelected = MutableStateFlow(false)
    val allAnswersSelected: StateFlow<Boolean> = _allAnswersSelected

    init {
        loadGame()
    }

    private fun loadGame() {
        viewModelScope.launch {
            try {
                val multipleChoiceGame = repository.getMultipleChoiceGameById(gameId)
                _game.value = multipleChoiceGame
                
                // Инициализируем ответы
                val initialAnswers = mutableMapOf<String, Int>()
                val initialSelectedOptions = mutableMapOf<String, Int>()
                
                multipleChoiceGame.questions.forEach { question ->
                    initialAnswers[question.id] = -1 // -1 означает, что ответ не выбран
                    initialSelectedOptions[question.id] = -1 // Нет выбранного варианта
                }
                
                _userAnswers.value = initialAnswers
                _selectedOptions.value = initialSelectedOptions
                checkAllAnswersSelected()
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    // Выбор варианта ответа
    fun selectAnswer(questionId: String, optionIndex: Int) {
        if (_answersChecked.value) return // Нельзя менять ответы после проверки
        
        val currentAnswers = _userAnswers.value.toMutableMap()
        val currentSelectedOptions = _selectedOptions.value.toMutableMap()
        
        // Если был выбран другой вариант, заменяем его
        currentAnswers[questionId] = optionIndex
        currentSelectedOptions[questionId] = optionIndex
        
        _userAnswers.value = currentAnswers
        _selectedOptions.value = currentSelectedOptions
        
        // Проверяем, все ли ответы выбраны
        checkAllAnswersSelected()
    }
    
    // Проверка, все ли ответы выбраны
    private fun checkAllAnswersSelected() {
        val game = _game.value ?: return
        val answers = _userAnswers.value
        
        // Проверяем, что для каждого вопроса в игре есть выбранный ответ
        val allSelected = game.questions.all { question ->
            val answerIndex = answers[question.id] ?: -1
            answerIndex >= 0
        }
        
        _allAnswersSelected.value = allSelected
    }

    // Проверка ответов
    fun checkAnswers(): Int {
        val game = _game.value ?: return 0

        // Если не все ответы выбраны, не проверяем
        if (!_allAnswersSelected.value) return 0

        val results = mutableMapOf<String, Boolean>()
        var correctCount = 0

        game.questions.forEach { question ->
            val userAnswerIndex = _userAnswers.value[question.id] ?: -1
            val isCorrect = userAnswerIndex == question.correctAnswerIndex
            results[question.id] = isCorrect
            if (isCorrect) correctCount++
        }

        _answerResults.value = results
        _score.value = correctCount
        _answersChecked.value = true

        return correctCount // Добавляем возврат значения
    }

    // Сброс игры
    fun resetGame() {
        val game = _game.value ?: return
        
        val initialAnswers = mutableMapOf<String, Int>()
        val initialSelectedOptions = mutableMapOf<String, Int>()
        
        game.questions.forEach { question ->
            initialAnswers[question.id] = -1
            initialSelectedOptions[question.id] = -1
        }
        
        _userAnswers.value = initialAnswers
        _selectedOptions.value = initialSelectedOptions
        _answerResults.value = emptyMap()
        _score.value = 0
        _answersChecked.value = false
        _allAnswersSelected.value = false
    }

    class Factory(private val gameId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MultipleChoiceGameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MultipleChoiceGameViewModel(MultipleChoiceGameRepository(), gameId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 