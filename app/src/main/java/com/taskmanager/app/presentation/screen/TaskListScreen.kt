package com.taskmanager.app.presentation.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.presentation.components.filters.FilterBar
import com.taskmanager.app.presentation.components.filters.FilterState
import com.taskmanager.app.presentation.components.task.EmptyFilterState
import com.taskmanager.app.presentation.components.task.EmptyTaskState
import com.taskmanager.app.presentation.components.task.TaskListContent

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    categoryFilter: String = "",
    onTaskClick: (Task) -> Unit,
    onEditTask: (Task) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val allTasks by viewModel.tasks.collectAsState()

    // Manage filters (category passed from previous screen)
    var filterState by remember(categoryFilter) {
        mutableStateOf(
            if (categoryFilter.isNotEmpty())
                FilterState(selectedCategory = categoryFilter)
            else FilterState()
        )
    }

    val hasFilters = filterState.selectedCategory != null ||
            filterState.selectedPriority != null ||
            filterState.selectedStatus != null

    // Pre-filtered task list
    val filteredTasks by remember(allTasks, filterState) {
        derivedStateOf {
            allTasks.filter { task ->
                val categoryMatch = filterState.selectedCategory?.let { task.category == it } ?: true
                val priorityMatch = filterState.selectedPriority?.let { task.priority == it } ?: true
                val statusMatch = filterState.selectedStatus?.let { task.isCompleted == it } ?: true
                categoryMatch && priorityMatch && statusMatch
            }
        }
    }

    //filtered list into pending + completed
    val partitionedTasks = remember(filteredTasks) {
        derivedStateOf {
            filteredTasks.partition { !it.isCompleted }
        }
    }
    val pendingTasks = partitionedTasks.value.first
    val completedTasks = partitionedTasks.value.second

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        FilterBar(
            categories = categories,
            filterState = filterState,
            onFilterChange = { filterState = it },
            onClearFilters = { filterState = FilterState() },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when {
            allTasks.isEmpty() -> EmptyTaskState()
            filteredTasks.isEmpty() -> EmptyFilterState { filterState = FilterState() }
            else -> {
                TaskListContent(
                    pendingTasks = pendingTasks,
                    completedTasks = completedTasks,
                    hasFilters = hasFilters,
                    onTaskClick = onTaskClick,
                    onEditTask = onEditTask,
                    onToggleStatus = { updated -> viewModel.updateTask(updated) }
                )
            }
        }
    }
}
