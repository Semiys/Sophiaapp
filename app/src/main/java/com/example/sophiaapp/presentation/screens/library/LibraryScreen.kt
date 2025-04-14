package com.example.sophiaapp.presentation.screens.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sophiaapp.presentation.components.SearchBar
import com.example.sophiaapp.presentation.components.CourseCard
import com.example.sophiaapp.presentation.components.FilterBottomSheet
import com.example.sophiaapp.navigation.Screen
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavHostController
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.presentation.viewmodels.SearchViewModel

@Composable
fun LibraryScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavHostController,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory())
) {
    // Состояние поиска и фильтрации
    var showBottomSheet by remember { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val topics by viewModel.selectedTopics.collectAsState()
    val branches by viewModel.selectedBranches.collectAsState()
    val filteredCourses by viewModel.filteredCourses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = AppStrings.Library.PHILOSOPHY_BASICS,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        // Обновлённый SearchBar с вводом текста
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.setSearchQuery(it) },
            onSearch = { /* Можно добавить действие по нажатию на поиск */ },
            onFilterClick = { showBottomSheet = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение курсов
        if (filteredCourses.isEmpty() && searchQuery.isNotBlank()) {
            // Если ничего не найдено
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "По запросу \"$searchQuery\" ничего не найдено",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Отображение списка курсов
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredCourses) { course ->
                    CourseCard(
                        courseId = course.id,
                        title = course.title,
                        subtitle = course.subtitle,
                        onExploreClick = { id ->
                            navController.navigate(Screen.CourseDetail.createRoute(id))
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }

    // Обновлённый FilterBottomSheet с передачей состояния
    FilterBottomSheet(
        showBottomSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        topics = topics,
        branches = branches,
        onTopicSelected = { index, isSelected -> viewModel.updateTopic(index, isSelected) },
        onBranchSelected = { index, isSelected -> viewModel.updateBranch(index, isSelected) },
        onApplyFilters = {
            // Можно добавить дополнительное действие при применении фильтров
            // Фильтрация уже происходит реактивно при изменении темы или направления
        }
    )
}