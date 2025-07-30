package com.example.taskmanagementapp.domain.repository

import com.example.taskmanagementapp.domain.model.Quote
import com.example.taskmanagementapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    fun getAllTasks(): Flow<List<Task>>

    suspend fun getTaskById(id: Int): Task?

    suspend fun getAllCategories(): List<String>

    fun getTasksByCategory(category: String): Flow<List<Task>>

    fun getTasksByPriority(priority: String): Flow<List<Task>>

    fun getTasksByStatus(completed: Boolean): Flow<List<Task>>

    suspend fun getRandomQuote(): Quote

    // New category management methods
    suspend fun addCategory(category: String)

    suspend fun deleteCategory(category: String)

    suspend fun updateCategory(oldCategory: String, newCategory: String)
}