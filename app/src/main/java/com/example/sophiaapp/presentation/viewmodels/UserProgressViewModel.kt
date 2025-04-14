package com.example.sophiaapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sophiaapp.data.repository.FirebaseAuthRepository
import com.example.sophiaapp.data.repository.UserProgressRepository
import com.example.sophiaapp.domain.models.LocationType
import com.example.sophiaapp.domain.models.UserLocation
import com.example.sophiaapp.domain.models.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProgressViewModel(
    private val progressRepository: UserProgressRepository,
    private val firebaseRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _userProgress = MutableStateFlow<UserProgress?>(null)
    val userProgress: StateFlow<UserProgress?> = _userProgress

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUserProgress()
    }

    private fun loadUserProgress() {
        val userId = firebaseRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Сначала загружаем данные из локальной БД
                val localProgress = progressRepository.getUserProgress(userId)
                _userProgress.value = localProgress

                // Затем синхронизируем с Firebase
                if (firebaseRepository.isUserLoggedIn()) {
                    progressRepository.syncProgressFromFirebase(userId).onSuccess { firebaseProgress ->
                        if (firebaseProgress != null) {
                            _userProgress.value = firebaseProgress
                        }
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Обновить прогресс курса
     */
    fun updateCourseProgress(
        courseId: String,
        theoryCompleted: Boolean? = null,
        practiceCompleted: Boolean? = null
    ) {
        val userId = firebaseRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            progressRepository.updateCourseProgress(
                userId = userId,
                courseId = courseId,
                theoryCompleted = theoryCompleted,
                practiceCompleted = practiceCompleted
            )
        }
    }

    /**
     * Обновить последнее местоположение пользователя
     */
    fun updateLastLocation(
        locationType: LocationType,
        courseId: String,
        sectionId: String
    ) {
        val userId = firebaseRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            progressRepository.updateLastLocation(
                userId = userId,
                locationType = locationType,
                courseId = courseId,
                sectionId = sectionId
            )
        }
    }

    /**
     * Получить последнее местоположение пользователя
     */
    fun getLastLocation(): UserLocation {
        return _userProgress.value?.lastLocation ?: UserLocation()
    }

    /**
     * Получить общий прогресс пользователя (0.0-1.0)
     */
    fun getOverallProgress(): Float {
        return _userProgress.value?.overallProgress ?: 0f
    }
    /**
     * Обновить результаты ответов пользователя
     */
    fun updateCourseAnswers(
        courseId: String,
        correctAnswers: Int,
        totalAnswers: Int
    ) {
        val userId = firebaseRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            progressRepository.updateCourseAnswers(
                userId = userId,
                courseId = courseId,
                correctAnswers = correctAnswers,
                totalAnswers = totalAnswers
            )
        }
    }

    /**
     * Получить детальный прогресс, включающий правильные ответы
     */
    fun getDetailedProgress(): Float {
        val progress = userProgress.value ?: return 0f

        // Расчет базового прогресса (теория + практика)
        val totalCourses = 8 // Только реализованные курсы
        var completedTheoryCount = 0
        var completedPracticeCount = 0

        progress.courseProgress.forEach { (courseId, data) ->
            if (courseId.toIntOrNull() in 1..8) {
                if (data.theoryCompleted) completedTheoryCount++
                if (data.practiceCompleted) completedPracticeCount++
            }
        }

        val totalSections = totalCourses * 2
        val completedSections = completedTheoryCount + completedPracticeCount
        val baseProgress = completedSections.toFloat() / totalSections

        // Расчет прогресса ответов
        var totalAnswers = 0
        var correctAnswers = 0

        progress.courseAnswers.forEach { (courseId, answers) ->
            if (courseId.toIntOrNull() in 1..8) {
                totalAnswers += answers.totalAnswers
                correctAnswers += answers.correctAnswers
            }
        }

        val answersProgress = if (totalAnswers > 0) correctAnswers.toFloat() / totalAnswers else 0f

        // Комбинированный прогресс (70% базовый + 30% ответы)
        return (baseProgress * 0.7f) + (answersProgress * 0.3f)
    }

    fun isCourseTheoryCompleted(courseId: String): Boolean {
        val progress = userProgress.value?.courseProgress?.get(courseId)
        return progress?.theoryCompleted ?: false
    }

    fun isCoursePracticeCompleted(courseId: String): Boolean {
        val progress = userProgress.value?.courseProgress?.get(courseId)
        return progress?.practiceCompleted ?: false
    }

    class Factory(
        private val progressRepository: UserProgressRepository,
        private val firebaseRepository: FirebaseAuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserProgressViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserProgressViewModel(progressRepository, firebaseRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}