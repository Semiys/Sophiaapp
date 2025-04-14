package com.example.sophiaapp.data.repository

import android.util.Log
import com.example.sophiaapp.data.local.preferences.UserProgressDao
import com.example.sophiaapp.data.local.preferences.toEntity
import com.example.sophiaapp.domain.models.CourseProgress
import com.example.sophiaapp.domain.models.LocationType
import com.example.sophiaapp.domain.models.UserLocation
import com.example.sophiaapp.domain.models.UserProgress
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.example.sophiaapp.domain.models.CourseAnswers

/**
 * Репозиторий для работы с прогрессом пользователя
 * Обеспечивает сохранение прогресса локально и в Firebase
 */
class UserProgressRepository(
    private val userProgressDao: UserProgressDao,
    private val firebaseRepository: FirebaseAuthRepository
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val progressCollection = firestore.collection("user_progress")

    /**
     * Получить прогресс пользователя как Flow
     */
    fun getUserProgressFlow(userId: String): Flow<UserProgress?> {
        return userProgressDao.getUserProgressFlow(userId)
            .map { entity -> entity?.toUserProgress() }
    }

    /**
     * Получить прогресс пользователя
     */
    suspend fun getUserProgress(userId: String): UserProgress? {
        return userProgressDao.getUserProgress(userId)?.toUserProgress()
    }

    /**
     * Сохранить прогресс пользователя
     */
    suspend fun saveUserProgress(progress: UserProgress): Result<Unit> {
        return try {
            val entity = progress.toEntity()
            userProgressDao.insertUserProgress(entity)

            // Если пользователь залогинен, синхронизируем с Firebase
            if (firebaseRepository.isUserLoggedIn()) {
                syncProgressToFirebase(progress)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserProgressRepository", "Error saving user progress", e)
            Result.failure(e)
        }
    }

    /**
     * Обновить прогресс курса
     */
    suspend fun updateCourseProgress(
        userId: String,
        courseId: String,
        theoryCompleted: Boolean? = null,
        practiceCompleted: Boolean? = null
    ): Result<Unit> {
        return try {
            val currentProgress = userProgressDao.getUserProgress(userId)?.toUserProgress()
                ?: UserProgress(userId = userId)

            val courseProgress = currentProgress.courseProgress[courseId] ?: CourseProgress(courseId = courseId)

            val updatedCourseProgress = courseProgress.copy(
                theoryCompleted = theoryCompleted ?: courseProgress.theoryCompleted,
                practiceCompleted = practiceCompleted ?: courseProgress.practiceCompleted,
                lastAccessTime = System.currentTimeMillis()
            )

            val updatedCourseProgressMap = currentProgress.courseProgress.toMutableMap().apply {
                put(courseId, updatedCourseProgress)
            }

            // Рассчитываем общий прогресс
            val totalItems = updatedCourseProgressMap.size * 2 // Теория + практика для каждого курса
            val completedItems = updatedCourseProgressMap.values.sumOf {
                (if (it.theoryCompleted) 1 else 0) + (if (it.practiceCompleted) 1 else 0)
            }

            val overallProgress = if (totalItems > 0) {
                completedItems.toFloat() / totalItems
            } else {
                0f
            }

            val updatedProgress = currentProgress.copy(
                courseProgress = updatedCourseProgressMap,
                overallProgress = overallProgress
            )

            saveUserProgress(updatedProgress)
        } catch (e: Exception) {
            Log.e("UserProgressRepository", "Error updating course progress", e)
            Result.failure(e)
        }
    }

    /**
     * Обновить последнее местоположение пользователя
     */
    suspend fun updateLastLocation(
        userId: String,
        locationType: LocationType,
        courseId: String,
        sectionId: String
    ): Result<Unit> {
        return try {
            val currentProgress = userProgressDao.getUserProgress(userId)?.toUserProgress()
                ?: UserProgress(userId = userId)

            val updatedLocation = UserLocation(
                type = locationType,
                courseId = courseId,
                sectionId = sectionId
            )

            val updatedProgress = currentProgress.copy(
                lastLocation = updatedLocation
            )

            saveUserProgress(updatedProgress)
        } catch (e: Exception) {
            Log.e("UserProgressRepository", "Error updating last location", e)
            Result.failure(e)
        }
    }

    /**
     * Синхронизировать прогресс с Firebase
     */
    private suspend fun syncProgressToFirebase(progress: UserProgress): Result<Unit> {
        val userId = firebaseRepository.getCurrentUserId() ?: return Result.failure(Exception("Пользователь не авторизован"))

        val progressData = hashMapOf(
            "courseProgress" to progress.courseProgress,
            "lastLocation" to hashMapOf(
                "type" to progress.lastLocation.type.name,
                "courseId" to progress.lastLocation.courseId,
                "sectionId" to progress.lastLocation.sectionId
            ),
            "courseAnswers" to progress.courseAnswers, // Убедитесь, что это поле включено
            "overallProgress" to progress.overallProgress,
            "updatedAt" to System.currentTimeMillis()
        )


        return try {
            progressCollection.document(userId).set(progressData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserProgressRepository", "Error syncing progress to Firebase", e)
            Result.failure(e)
        }
    }


    /**
     * Загрузить прогресс из Firebase
     */
    /**
     * Загрузить прогресс из Firebase
     */
    suspend fun syncProgressFromFirebase(userId: String): Result<UserProgress?> {
        return try {
            val document = progressCollection.document(userId).get().await()

            if (document.exists()) {
                // Безопасное приведение типов для courseProgress
                @Suppress("UNCHECKED_CAST")
                val courseProgressData = try {
                    val data = document.get("courseProgress")
                    if (data is Map<*, *>) {
                        // Проверяем, что это карта с правильной структурой
                        data as Map<String, Map<String, Any>>
                    } else {
                        Log.e("UserProgressRepository", "courseProgress is not a Map: ${data?.javaClass?.name}")
                        emptyMap()
                    }
                } catch (e: Exception) {
                    Log.e("UserProgressRepository", "Error processing courseProgress data", e)
                    emptyMap<String, Map<String, Any>>()
                }

                val courseProgress = courseProgressData.mapValues { (_, value) ->
                    CourseProgress(
                        courseId = value["courseId"] as? String ?: "",
                        theoryCompleted = value["theoryCompleted"] as? Boolean ?: false,
                        practiceCompleted = value["practiceCompleted"] as? Boolean ?: false,
                        lastAccessTime = (value["lastAccessTime"] as? Long) ?: 0
                    )
                }

                // Безопасное приведение типов для lastLocation с подавлением предупреждения
                @Suppress("UNCHECKED_CAST")
                val lastLocationData = try {
                    val data = document.get("lastLocation")
                    if (data is Map<*, *>) {
                        data as Map<String, Any>
                    } else {
                        Log.e("UserProgressRepository", "lastLocation is not a Map: ${data?.javaClass?.name}")
                        emptyMap()
                    }
                } catch (e: Exception) {
                    Log.e("UserProgressRepository", "Error processing lastLocation data", e)
                    emptyMap<String, Any>()
                }

                val lastLocation = UserLocation(
                    type = try {
                        LocationType.valueOf(lastLocationData["type"] as? String ?: "NONE")
                    } catch (e: IllegalArgumentException) {
                        Log.e("UserProgressRepository", "Invalid location type", e)
                        LocationType.NONE
                    },
                    courseId = lastLocationData["courseId"] as? String ?: "",
                    sectionId = lastLocationData["sectionId"] as? String ?: ""
                )

                // Получаем данные courseAnswers
                @Suppress("UNCHECKED_CAST")
                val courseAnswersData = try {
                    val data = document.get("courseAnswers")
                    if (data is Map<*, *>) {
                        data as Map<String, Map<String, Any>>
                    } else {
                        Log.e("UserProgressRepository", "courseAnswers is not a Map: ${data?.javaClass?.name}")
                        emptyMap()
                    }
                } catch (e: Exception) {
                    Log.e("UserProgressRepository", "Error processing courseAnswers data", e)
                    emptyMap<String, Map<String, Any>>()
                }

                // Преобразуем данные в модель CourseAnswers
                val courseAnswers = courseAnswersData.mapValues { (_, value) ->
                    CourseAnswers(
                        correctAnswers = (value["correctAnswers"] as? Number)?.toInt() ?: 0,
                        totalAnswers = (value["totalAnswers"] as? Number)?.toInt() ?: 0
                    )
                }

                val overallProgress = (document.get("overallProgress") as? Double)?.toFloat() ?: 0f

                val progress = UserProgress(
                    userId = userId,
                    courseProgress = courseProgress,
                    lastLocation = lastLocation,
                    overallProgress = overallProgress,
                    courseAnswers = courseAnswers  // Добавляем courseAnswers
                )

                // Сохраняем полученный прогресс локально
                saveUserProgress(progress)

                Result.success(progress)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e("UserProgressRepository", "Error syncing progress from Firebase", e)
            Result.failure(e)
        }
    }
    /**
     * Обновить результаты ответов пользователя
     */
    suspend fun updateCourseAnswers(
        userId: String,
        courseId: String,
        correctAnswers: Int,
        totalAnswers: Int
    ): Result<Unit> {
        return try {
            val currentProgress = userProgressDao.getUserProgress(userId)?.toUserProgress()
                ?: UserProgress(userId = userId)

            // Получаем существующие данные ответов или создаем новые
            val currentAnswers = currentProgress.courseAnswers.toMutableMap()
            val existingAnswers = currentAnswers[courseId] ?: CourseAnswers()

            // Важное изменение: вместо добавления к старым значениям, создаем новые
            // так как игра может проходиться несколько раз
            val updatedAnswers = CourseAnswers(
                correctAnswers = correctAnswers, // Используем текущие значения, не складываем
                totalAnswers = totalAnswers     // Используем текущие значения, не складываем
            )

            currentAnswers[courseId] = updatedAnswers

            // Обновляем общий прогресс с учетом правильных ответов
            val courseCompletionProgress = calculateCompletionProgress(currentProgress.courseProgress)
            val answersProgress = calculateAnswersProgress(currentAnswers)

            val overallProgress = (courseCompletionProgress * 0.7f) + (answersProgress * 0.3f)

            val updatedProgress = currentProgress.copy(
                courseAnswers = currentAnswers,
                overallProgress = overallProgress
            )

            saveUserProgress(updatedProgress)
        } catch (e: Exception) {
            Log.e("UserProgressRepository", "Error updating course answers", e)
            Result.failure(e)
        }
    }

    // Метод расчета прогресса на основе завершенных теорий и практик
    private fun calculateCompletionProgress(courseProgress: Map<String, CourseProgress>): Float {
        val validCourses = courseProgress.filter { (courseId, _) ->
            courseId.toIntOrNull() in 1..8
        }

        val totalItems = validCourses.size * 2 // Теория + практика для каждого курса
        val completedItems = validCourses.values.sumOf { courseProgress ->
            (if (courseProgress.theoryCompleted) 1 else 0) + (if (courseProgress.practiceCompleted) 1 else 0)
        }

        return if (totalItems > 0) completedItems.toFloat() / totalItems else 0f
    }

    // Метод расчета прогресса на основе правильных ответов
    private fun calculateAnswersProgress(answers: Map<String, CourseAnswers>): Float {
        val validAnswers = answers.filter { (courseId, _) ->
            courseId.toIntOrNull() in 1..8
        }

        val totalAnswers = validAnswers.values.sumOf { courseAnswers -> courseAnswers.totalAnswers }
        val correctAnswers = validAnswers.values.sumOf { courseAnswers -> courseAnswers.correctAnswers }

        return if (totalAnswers > 0) correctAnswers.toFloat() / totalAnswers else 0f
    }

}