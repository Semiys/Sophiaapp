package com.example.sophiaapp.domain.models



data class CourseAnswers(
    val correctAnswers: Int = 0,
    val totalAnswers: Int = 0
)
/**
 * Модель данных для хранения прогресса пользователя
 */
data class UserProgress(
    val userId: String = "",
    val courseProgress: Map<String, CourseProgress> = emptyMap(),
    val lastLocation: UserLocation = UserLocation(),
    val overallProgress: Float = 0f,
    val courseAnswers: Map<String, CourseAnswers> = emptyMap() // Новое поле
)

/**
 * Представляет местоположение пользователя в приложении
 */
data class UserLocation(
    val type: LocationType = LocationType.NONE,
    val courseId: String = "",
    val sectionId: String = ""
)

/**
 * Тип местоположения пользователя
 */
enum class LocationType {
    NONE,
    THEORY,
    PRACTICE
}

/**
 * Прогресс по конкретному курсу
 */
data class CourseProgress(
    val courseId: String = "",
    val theoryCompleted: Boolean = false,
    val practiceCompleted: Boolean = false,
    val lastAccessTime: Long = 0 // Время последнего доступа
)

