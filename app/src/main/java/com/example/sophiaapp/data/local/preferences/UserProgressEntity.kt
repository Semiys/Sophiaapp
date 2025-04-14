package com.example.sophiaapp.data.local.preferences

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.sophiaapp.domain.models.CourseAnswers
import com.example.sophiaapp.domain.models.CourseProgress
import com.example.sophiaapp.domain.models.LocationType
import com.example.sophiaapp.domain.models.UserLocation
import com.example.sophiaapp.domain.models.UserProgress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "user_progress")
@TypeConverters(ProgressConverters::class)
data class UserProgressEntity(
    @PrimaryKey
    val userId: String,
    val courseProgressJson: String, // Карта курсов в JSON
    val lastLocationType: String, // Тип местоположения как строка
    val lastLocationCourseId: String, // Курс ID последнего местоположения
    val lastLocationSectionId: String, // Секция ID последнего местоположения
    val overallProgress: Float, // Общий прогресс 0.0-1.0
    val courseAnswersJson: String = "" // Добавляем поле для ответов на курсы
) {
    /**
     * Преобразование в доменную модель
     */
    fun toUserProgress(): UserProgress {
        val gson = Gson()
        val progressType = object : TypeToken<Map<String, CourseProgress>>() {}.type
        val answersType = object : TypeToken<Map<String, CourseAnswers>>() {}.type

        val courseProgress: Map<String, CourseProgress> =
            if (courseProgressJson.isNotEmpty()) gson.fromJson(courseProgressJson, progressType) else emptyMap()

        val courseAnswers: Map<String, CourseAnswers> =
            if (courseAnswersJson.isNotEmpty()) gson.fromJson(courseAnswersJson, answersType) else emptyMap()

        val locationType = try {
            LocationType.valueOf(lastLocationType)
        } catch (e: IllegalArgumentException) {
            LocationType.NONE
        }

        return UserProgress(
            userId = userId,
            courseProgress = courseProgress,
            lastLocation = UserLocation(
                type = locationType,
                courseId = lastLocationCourseId,
                sectionId = lastLocationSectionId
            ),
            overallProgress = overallProgress,
            courseAnswers = courseAnswers
        )
    }
}

/**
 * Преобразование доменной модели в сущность
 */
fun UserProgress.toEntity(): UserProgressEntity {
    val gson = Gson()
    val courseProgressJson = gson.toJson(courseProgress)
    val courseAnswersJson = gson.toJson(courseAnswers)

    return UserProgressEntity(
        userId = userId,
        courseProgressJson = courseProgressJson,
        lastLocationType = lastLocation.type.name,
        lastLocationCourseId = lastLocation.courseId,
        lastLocationSectionId = lastLocation.sectionId,
        overallProgress = overallProgress,
        courseAnswersJson = courseAnswersJson
    )
}

/**
 * Конвертеры для Room
 */
class ProgressConverters {
    @TypeConverter
    fun fromMapToString(map: Map<String, CourseProgress>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun fromStringToMap(value: String): Map<String, CourseProgress> {
        val type = object : TypeToken<Map<String, CourseProgress>>() {}.type
        return Gson().fromJson(value, type)
    }
}