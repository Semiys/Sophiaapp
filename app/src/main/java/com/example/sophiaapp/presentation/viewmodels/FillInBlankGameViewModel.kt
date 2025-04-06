package com.example.sophiaapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sophiaapp.data.repository.FillInBlankGameRepository
import com.example.sophiaapp.domain.models.FillInBlankGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FillInBlankGameViewModel(
    private val repository: FillInBlankGameRepository,
    private val gameId: String
) : ViewModel() {

    private val _game = MutableStateFlow<FillInBlankGame?>(null)
    val game: StateFlow<FillInBlankGame?> = _game

    // Ответы пользователя: ID вопроса -> текст ответа
    private val _userAnswers = MutableStateFlow<Map<String, String>>(emptyMap())
    val userAnswers: StateFlow<Map<String, String>> = _userAnswers

    // Флаг, показывающий, проверены ли ответы
    private val _answersChecked = MutableStateFlow(false)
    val answersChecked: StateFlow<Boolean> = _answersChecked

    // Правильные/неправильные ответы: ID вопроса -> результат проверки
    private val _answerResults = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val answerResults: StateFlow<Map<String, Boolean>> = _answerResults

    // Общий результат - количество правильных ответов
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    init {
        loadGame()
    }

    private fun loadGame() {
        viewModelScope.launch {
            try {
                val fillInBlankGame = repository.getFillInBlankGameById(gameId)
                _game.value = fillInBlankGame
                
                // Инициализируем ответы пустыми строками
                val initialAnswers = fillInBlankGame.questions.associate { it.id to "" }
                _userAnswers.value = initialAnswers
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    // Обновление ответа пользователя
    fun updateAnswer(questionId: String, answer: String) {
        if (_answersChecked.value) return // Нельзя менять ответы после проверки
        
        val currentAnswers = _userAnswers.value.toMutableMap()
        currentAnswers[questionId] = answer
        _userAnswers.value = currentAnswers
    }

    // Проверка ответов
    fun checkAnswers() {
        val game = _game.value ?: return
        val results = mutableMapOf<String, Boolean>()
        var correctCount = 0

        game.questions.forEach { question ->
            val userAnswer = _userAnswers.value[question.id] ?: ""
            val isCorrect = userAnswer.trim().equals(question.correctAnswer, ignoreCase = true)
            results[question.id] = isCorrect
            if (isCorrect) correctCount++
        }

        _answerResults.value = results
        _score.value = correctCount
        _answersChecked.value = true
    }

    // Сброс игры
    fun resetGame() {
        val game = _game.value ?: return
        val initialAnswers = game.questions.associate { it.id to "" }
        _userAnswers.value = initialAnswers
        _answerResults.value = emptyMap()
        _score.value = 0
        _answersChecked.value = false
    }

    class Factory(private val gameId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FillInBlankGameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FillInBlankGameViewModel(FillInBlankGameRepository(), gameId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 