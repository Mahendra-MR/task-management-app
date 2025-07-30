package com.example.taskmanagementapp.data.repository

import com.example.taskmanagementapp.data.local.dao.CategoryDao
import com.example.taskmanagementapp.data.local.dao.QuoteDao
import com.example.taskmanagementapp.data.local.dao.TaskDao
import com.example.taskmanagementapp.data.local.entities.CategoryEntity
import com.example.taskmanagementapp.data.local.entities.QuoteEntity
import com.example.taskmanagementapp.data.local.entities.toDomain
import com.example.taskmanagementapp.data.local.entities.toEntity
import com.example.taskmanagementapp.data.remote.QuoteRemoteSource
import com.example.taskmanagementapp.domain.model.Quote
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val quoteRemoteSource: QuoteRemoteSource,
    private val quoteDao: QuoteDao
) : TaskRepository {

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
        categoryDao.insertCategory(CategoryEntity(task.category))
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
        categoryDao.insertCategory(CategoryEntity(task.category))
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun getAllCategories(): List<String> {
        val taskCategories = taskDao.getAllCategories()
        val storedCategories = categoryDao.getAllCategories().map { it.name }
        return (taskCategories + storedCategories).distinct().sorted()
    }

    override fun getTasksByCategory(category: String): Flow<List<Task>> {
        return taskDao.getTasksByCategory(category).map { it.map { entity -> entity.toDomain() } }
    }

    override fun getTasksByPriority(priority: String): Flow<List<Task>> {
        return taskDao.getTasksByPriority(priority).map { it.map { entity -> entity.toDomain() } }
    }

    override fun getTasksByStatus(completed: Boolean): Flow<List<Task>> {
        return taskDao.getTasksByStatus(completed).map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun getRandomQuote(): Quote {
        return try {
            val quote = quoteRemoteSource.fetchRandomQuote()
            quoteDao.insertQuote(
                QuoteEntity(
                    id = 1,
                    content = quote.content,
                    author = quote.author
                )
            )
            quote
        } catch (e: Exception) {
            quoteDao.getCachedQuote()?.let {
                Quote(content = it.content, author = it.author)
            } ?: throw e
        }
    }

    override suspend fun addCategory(category: String) {
        categoryDao.insertCategory(CategoryEntity(category))
    }

    override suspend fun deleteCategory(category: String) {
        categoryDao.deleteCategoryByName(category)
    }

    override suspend fun updateCategory(oldCategory: String, newCategory: String) {
        taskDao.updateTasksCategory(oldCategory, newCategory)
        categoryDao.deleteCategoryByName(oldCategory)
        categoryDao.insertCategory(CategoryEntity(newCategory))
    }
}
