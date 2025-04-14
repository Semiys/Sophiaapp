package com.example.sophiaapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sophiaapp.data.repository.CourseRepository
import com.example.sophiaapp.domain.models.Topic
import com.example.sophiaapp.domain.models.Branch
import com.example.sophiaapp.utils.localization.AppStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedTopics = MutableStateFlow<List<Topic>>(emptyList())
    val selectedTopics: StateFlow<List<Topic>> = _selectedTopics

    private val _selectedBranches = MutableStateFlow<List<Branch>>(emptyList())
    val selectedBranches: StateFlow<List<Branch>> = _selectedBranches

    // Добавляем блок инициализации
    init {
        // Инициализация тем
        _selectedTopics.value = listOf(
            Topic(AppStrings.FilterBottomSheet.EPISTEMOLOGY),
            Topic(AppStrings.FilterBottomSheet.ETHICS),
            Topic(AppStrings.FilterBottomSheet.LOGIC),
            Topic(AppStrings.FilterBottomSheet.METAPHYSICS),
            Topic(AppStrings.FilterBottomSheet.POLITICAL_PHILOSOPHY)
        )

        // Инициализация направлений
        _selectedBranches.value = listOf(
            Branch(AppStrings.FilterBottomSheet.PHILOSOPHY_LANGUAGE),
            Branch(AppStrings.FilterBottomSheet.PHILOSOPHY_SCIENCE),
            Branch(AppStrings.FilterBottomSheet.ETHICAL_THEORIES),
            Branch(AppStrings.FilterBottomSheet.PHILOSOPHY_TECHNOLOGY)
        )
    }

    // Комбинируем результаты поиска и фильтрации
    val filteredCourses = combine(
        _searchQuery,
        _selectedTopics,
        _selectedBranches,
        courseRepository.allCourses
    ) { query, topics, branches, allCourses ->
        var result = allCourses

        // Применяем поиск
        if (query.isNotBlank()) {
            result = result.filter { it.title.contains(query, ignoreCase = true) }
        }

        // Применяем фильтры
        val activeTopics = topics.filter { it.isSelected }
        val activeBranches = branches.filter { it.isSelected }

        if (activeTopics.isNotEmpty() || activeBranches.isNotEmpty()) {
            result = result.filter { course ->
                val topicMatch = activeTopics.isEmpty() ||
                        course.topics.any { topic -> activeTopics.any { it.name == topic } }

                val branchMatch = activeBranches.isEmpty() ||
                        activeBranches.any { it.name == course.branch }

                topicMatch || branchMatch
            }
        }

        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setTopics(topics: List<Topic>) {
        _selectedTopics.value = topics
    }

    fun updateTopic(index: Int, isSelected: Boolean) {
        val updatedTopics = _selectedTopics.value.toMutableList()
        if (index < updatedTopics.size) {
            updatedTopics[index] = updatedTopics[index].copy(isSelected = isSelected)
            _selectedTopics.value = updatedTopics
        }
    }

    fun setBranches(branches: List<Branch>) {
        _selectedBranches.value = branches
    }

    fun updateBranch(index: Int, isSelected: Boolean) {
        val updatedBranches = _selectedBranches.value.toMutableList()
        if (index < updatedBranches.size) {
            updatedBranches[index] = updatedBranches[index].copy(isSelected = isSelected)
            _selectedBranches.value = updatedBranches
        }
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(
                    courseRepository = CourseRepository.getInstance()
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}