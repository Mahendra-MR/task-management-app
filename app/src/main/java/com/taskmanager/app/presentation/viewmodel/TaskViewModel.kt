package com.taskmanager.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.app.domain.model.Quote
import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException
import com.taskmanager.app.domain.model.Priority

class TaskViewModel(
    private val useCases: TaskUseCases
) : ViewModel() {

    // 🔹 Split StateFlows (instead of big TaskUiState)
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _highPriorityTasks = MutableStateFlow<List<Task>>(emptyList())
    val highPriorityTasks: StateFlow<List<Task>> = _highPriorityTasks.asStateFlow()


    init {
        loadTasks()
        loadCategories()
        loadQuote()
    }


    fun loadTasks() {
        viewModelScope.launch {
            useCases.getAllTasks()
                .distinctUntilChanged()
                .collect { taskList ->
                    _tasks.value = taskList
                    _highPriorityTasks.value = taskList.filter {
                        it.priority == Priority.HIGH && !it.isCompleted
                    }
                }
        }
    }

    fun loadQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                Log.d("TaskViewModel", "Loading quote...")
                val quoteResult = useCases.getQuote()
                _quote.value = quoteResult
                Log.d("TaskViewModel", "Quote loaded: ${quoteResult.content}")
            } catch (e: UnknownHostException) {
                handleError("No internet connection", e)
            } catch (e: IOException) {
                handleError("Network error. Try again.", e)
            } catch (e: Exception) {
                handleError("Unexpected error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleError(message: String, exception: Exception) {
        Log.e("TaskViewModel", message, exception)
        _error.value = message
    }

    fun retryLoadQuote() = loadQuote()

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val result = useCases.getCategories()
                _categories.value = result
                Log.d("TaskViewModel", "Categories loaded: $result")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to load categories", e)
            }
        }
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            try {
                val task = useCases.getTaskById(id)
                _selectedTask.value = task
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to get task by ID", e)
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                useCases.addTask(task)
                refreshData()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to add task", e)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                useCases.updateTask(task)
                refreshData()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to update task", e)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                useCases.deleteTask(task)
                loadTasks()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to delete task", e)
            }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            try {
                useCases.filterTasks.byCategory(category)
                    .distinctUntilChanged()
                    .collect { filtered ->
                        _tasks.value = filtered
                    }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to filter by category", e)
            }
        }
    }

    fun filterByPriority(priority: String) {
        viewModelScope.launch {
            try {
                useCases.filterTasks.byPriority(priority)
                    .distinctUntilChanged()
                    .collect { filtered ->
                        _tasks.value = filtered
                    }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to filter by priority", e)
            }
        }
    }

    fun filterByStatus(isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                useCases.filterTasks.byStatus(isCompleted)
                    .distinctUntilChanged()
                    .collect { filtered ->
                        _tasks.value = filtered
                    }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to filter by status", e)
            }
        }
    }

    fun addCategory(category: String) {
        viewModelScope.launch {
            try {
                useCases.addCategory(category)
                loadCategories()
                Log.d("TaskViewModel", "Category added: $category")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to add category", e)
            }
        }
    }

    fun deleteCategory(category: String) {
        viewModelScope.launch {
            try {
                useCases.deleteCategory(category)
                loadCategories()
                Log.d("TaskViewModel", "Category deleted: $category")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to delete category", e)
            }
        }
    }

    fun updateCategory(oldCategory: String, newCategory: String) {
        viewModelScope.launch {
            try {
                useCases.updateCategory(oldCategory, newCategory)
                refreshData()
                Log.d("TaskViewModel", "Category updated from $oldCategory to $newCategory")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to update category", e)
            }
        }
    }

    fun refreshData() {
        loadTasks()
    }
}
