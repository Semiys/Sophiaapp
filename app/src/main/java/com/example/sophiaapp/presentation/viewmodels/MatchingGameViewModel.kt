package com.example.sophiaapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sophiaapp.data.repository.MatchingGameRepository
import com.example.sophiaapp.domain.models.MatchingGame
import com.example.sophiaapp.domain.models.MatchingPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MatchingGameViewModel(
    private val repository: MatchingGameRepository,
    private val gameId: String
) : ViewModel() {

    private val _game = MutableStateFlow<MatchingGame?>(null)
    val game: StateFlow<MatchingGame?> = _game

    private val _userPairs = MutableStateFlow<List<MatchingPair>>(emptyList())
    val userPairs: StateFlow<List<MatchingPair>> = _userPairs

    // Флаг, показывающий, что все вопросы соединены (но не обязательно правильно)
    private val _isGameCompleted = MutableStateFlow(false)
    val isGameCompleted: StateFlow<Boolean> = _isGameCompleted

    // Новый флаг, который показывает, что результаты должны отображаться (после нажатия "Проверить")
    private val _showResults = MutableStateFlow(false)
    val showResults: StateFlow<Boolean> = _showResults

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    init {
        loadGame()
    }

    private fun loadGame() {
        viewModelScope.launch {
            try {
                val matchingGame = repository.getMatchingGameById(gameId)
                _game.value = matchingGame
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun createConnection(questionId: String, answerId: String) {
        // Проверяем, что вопрос ещё не соединен
        val existingQuestionConnection = _userPairs.value.find { it.questionId == questionId }
        if (existingQuestionConnection != null) {
            // Если уже есть соединение для этого вопроса, удаляем его
            removeConnection(questionId)
        }

        // Проверяем, что ответ ещё не соединен
        val existingAnswerConnection = _userPairs.value.find { it.answerId == answerId }
        if (existingAnswerConnection != null) {
            // Если уже есть соединение для этого ответа, удаляем его
            removeConnectionByAnswerId(answerId)
        }

        // Добавляем новое соединение
        val newPairs = _userPairs.value.toMutableList()
        val isCorrect = _game.value?.correctPairs?.any {
            it.questionId == questionId && it.answerId == answerId
        } ?: false

        newPairs.add(MatchingPair(questionId, answerId, isCorrect))
        _userPairs.value = newPairs

        // Проверяем, завершена ли игра (все вопросы соединены)
        checkGameCompletion()
    }

    fun removeConnection(questionId: String) {
        val newPairs = _userPairs.value.filter { it.questionId != questionId }
        _userPairs.value = newPairs
        _isGameCompleted.value = checkAllQuestionsConnected()
    }

    // Новый метод для удаления соединения по ID ответа
    private fun removeConnectionByAnswerId(answerId: String) {
        val newPairs = _userPairs.value.filter { it.answerId != answerId }
        _userPairs.value = newPairs
        _isGameCompleted.value = checkAllQuestionsConnected()
    }

    fun checkAnswers(): Int {
        val game = _game.value ?: return 0
        val correctCount = _userPairs.value.count { pair ->
            game.correctPairs.any {
                it.questionId == pair.questionId && it.answerId == pair.answerId
            }
        }

        _score.value = correctCount
        _showResults.value = true

        return correctCount // Добавляем возврат значения
    }

    private fun checkGameCompletion() {
        _isGameCompleted.value = checkAllQuestionsConnected()
    }

    private fun checkAllQuestionsConnected(): Boolean {
        val game = _game.value ?: return false
        // Игра считается завершенной, когда пользователь сделал соединения для всех вопросов
        return _userPairs.value.size == game.questions.size
    }

    fun resetGame() {
        _userPairs.value = emptyList()
        _isGameCompleted.value = false
        _showResults.value = false
        _score.value = 0
    }

    class Factory(private val gameId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MatchingGameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MatchingGameViewModel(MatchingGameRepository(), gameId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}