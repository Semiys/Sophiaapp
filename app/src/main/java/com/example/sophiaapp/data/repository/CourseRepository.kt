package com.example.sophiaapp.data.repository

import com.example.sophiaapp.domain.models.Course
import com.example.sophiaapp.domain.models.Topic
import com.example.sophiaapp.domain.models.Branch
import com.example.sophiaapp.utils.localization.AppStrings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class CourseRepository {
    // Создаем модель курса
    data class Course(
        val id: String,
        val title: String,
        val subtitle: String = AppStrings.Library.EXPERT_INSTRUCTOR,
        val topics: List<String> = emptyList(),
        val branch: String = ""
    )

    // Список всех курсов
    private val _allCourses = MutableStateFlow(
        listOf(
            Course(
                id = "1",
                title = AppStrings.Course.COURSE_1_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.METAPHYSICS, AppStrings.FilterBottomSheet.LOGIC)
            ),
            Course(
                id = "2",
                title = AppStrings.Course.COURSE_2_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.LOGIC, AppStrings.FilterBottomSheet.EPISTEMOLOGY),
                branch = AppStrings.FilterBottomSheet.PHILOSOPHY_SCIENCE
            ),
            Course(
                id = "3",
                title = AppStrings.Course.COURSE_3_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.METAPHYSICS)
            ),
            Course(
                id = "4",
                title = AppStrings.Course.COURSE_4_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.METAPHYSICS, AppStrings.FilterBottomSheet.ETHICS),
                branch = AppStrings.FilterBottomSheet.PHILOSOPHY_SCIENCE
            ),
            Course(
                id = "5",
                title = AppStrings.Course.COURSE_5_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.LOGIC, AppStrings.FilterBottomSheet.METAPHYSICS)
            ),
            Course(
                id = "6",
                title = AppStrings.Course.COURSE_6_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.METAPHYSICS),
                branch = AppStrings.FilterBottomSheet.PHILOSOPHY_SCIENCE
            ),
            Course(
                id = "7",
                title = AppStrings.Course.COURSE_7_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.METAPHYSICS, AppStrings.FilterBottomSheet.LOGIC)
            ),
            Course(
                id = "8",
                title = AppStrings.Course.COURSE_8_TITLE,
                topics = listOf(AppStrings.FilterBottomSheet.METAPHYSICS, AppStrings.FilterBottomSheet.EPISTEMOLOGY),
                branch = AppStrings.FilterBottomSheet.ETHICAL_THEORIES
            )
        )
    )

    val allCourses: Flow<List<Course>> = _allCourses

    // Поиск курсов по строке запроса
    fun searchCourses(query: String): Flow<List<Course>> {
        return _allCourses.map { courses ->
            if (query.isBlank()) {
                courses
            } else {
                courses.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            }
        }
    }

    // Фильтрация курсов по выбранным темам и направлениям
    fun filterCourses(selectedTopics: List<Topic>, selectedBranches: List<Branch>): Flow<List<Course>> {
        return _allCourses.map { courses ->
            val activeTopics = selectedTopics.filter { it.isSelected }.map { it.name }
            val activeBranches = selectedBranches.filter { it.isSelected }.map { it.name }

            if (activeTopics.isEmpty() && activeBranches.isEmpty()) {
                // Если ничего не выбрано, возвращаем все курсы
                courses
            } else {
                courses.filter { course ->
                    // Проверяем соответствие выбранным темам
                    val matchesTopic = activeTopics.isEmpty() ||
                            course.topics.any { topic -> activeTopics.contains(topic) }

                    // Проверяем соответствие выбранным направлениям
                    val matchesBranch = activeBranches.isEmpty() ||
                            activeBranches.contains(course.branch)

                    // Курс должен соответствовать хотя бы одному из выбранных фильтров
                    matchesTopic || matchesBranch
                }
            }
        }
    }

    // Объединение поиска и фильтрации
    fun searchAndFilterCourses(
        query: String,
        selectedTopics: List<Topic>,
        selectedBranches: List<Branch>
    ): Flow<List<Course>> {
        return _allCourses.map { courses ->
            var filteredCourses = courses

            // Применяем поиск
            if (query.isNotBlank()) {
                filteredCourses = filteredCourses.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            }

            // Применяем фильтры по темам и направлениям
            val activeTopics = selectedTopics.filter { it.isSelected }.map { it.name }
            val activeBranches = selectedBranches.filter { it.isSelected }.map { it.name }

            if (activeTopics.isNotEmpty() || activeBranches.isNotEmpty()) {
                filteredCourses = filteredCourses.filter { course ->
                    // Проверяем соответствие выбранным темам
                    val matchesTopic = activeTopics.isEmpty() ||
                            course.topics.any { topic -> activeTopics.contains(topic) }

                    // Проверяем соответствие выбранным направлениям
                    val matchesBranch = activeBranches.isEmpty() ||
                            activeBranches.contains(course.branch)

                    // Курс должен соответствовать хотя бы одному из выбранных фильтров
                    matchesTopic || matchesBranch
                }
            }

            filteredCourses
        }
    }

    companion object {
        // Синглтон для доступа к репозиторию
        @Volatile
        private var INSTANCE: CourseRepository? = null

        fun getInstance(): CourseRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = CourseRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}