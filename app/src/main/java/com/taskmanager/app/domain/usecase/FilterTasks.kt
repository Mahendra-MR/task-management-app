package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import com.taskmanager.app.domain.model.Task

class FilterTasks(private val repository: TaskRepository) {
    fun byCategory(category: String): Flow<List<Task>> = repository.getTasksByCategory(category)
    fun byPriority(priority: String): Flow<List<Task>> = repository.getTasksByPriority(priority)
    fun byStatus(isCompleted: Boolean): Flow<List<Task>> = repository.getTasksByStatus(isCompleted)
}
