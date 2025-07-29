package com.example.taskmanagementapp.data.repository

import com.example.taskmanagementapp.data.local.dao.TaskDao
import com.example.taskmanagementapp.data.local.entities.toDomain
import com.example.taskmanagementapp.data.local.entities.toEntity
import com.example.taskmanagementapp.data.remote.QuoteRemoteSource
import com.example.taskmanagementapp.domain.model.Quote
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val dao: TaskDao,
    private val quoteRemoteSource: QuoteRemoteSource
) : TaskRepository {

    override suspend fun addTask(task: Task) {
        dao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task.toEntity())
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return dao.getTaskById(id)?.toDomain()
    }

    override suspend fun getAllCategories(): List<String> {
        return dao.getAllCategories()
    }

    override fun getTasksByCategory(category: String): Flow<List<Task>> {
        return dao.getTasksByCategory(category).map { it.map { entity -> entity.toDomain() } }
    }

    override fun getTasksByPriority(priority: String): Flow<List<Task>> {
        return dao.getTasksByPriority(priority).map { it.map { entity -> entity.toDomain() } }
    }

    override fun getTasksByStatus(completed: Boolean): Flow<List<Task>> {
        return dao.getTasksByStatus(completed).map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun getRandomQuote(): Quote {
        return quoteRemoteSource.fetchRandomQuote()
    }
}
