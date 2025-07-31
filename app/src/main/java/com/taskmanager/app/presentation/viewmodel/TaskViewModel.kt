package com.taskmanager.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.domain.model.Quote
import com.taskmanager.app.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val quote: Quote? = null,
    val categories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TaskViewModel(
    private val useCases: TaskUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TaskUiState())
    val state: StateFlow<TaskUiState> = _state.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        // Only load essential data - NO QUOTE LOADING HERE!
        refreshData()
        // loadQuote() - REMOVED FROM INIT!
    }

    fun loadTasks() {
        viewModelScope.launch {
            useCases.getAllTasks()
                .distinctUntilChanged()
                .collect { taskList ->
                    _tasks.value = taskList
                    _state.update { it.copy(tasks = taskList) }
                }
        }
    }

    // Quote loading method - now only called when needed
    fun loadQuote() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                Log.d("TaskViewModel", "Loading quote...")
                val quote = useCases.getQuote()
                Log.d("TaskViewModel", "Quote loaded: ${quote.content}")

                _state.update { it.copy(quote = quote, isLoading = false) }
            } catch (e: UnknownHostException) {
                handleError("No internet connection", e)
            } catch (e: IOException) {
                handleError("Network error. Try again.", e)
            } catch (e: Exception) {
                handleError("Unexpected error: ${e.message}", e)
            }
        }
    }

    private fun handleError(message: String, exception: Exception) {
        Log.e("TaskViewModel", message, exception)
        _state.update { it.copy(error = message, isLoading = false) }
    }

    fun retryLoadQuote() = loadQuote()

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = useCases.getCategories()
                _state.update { it.copy(categories = categories) }
                Log.d("TaskViewModel", "Categories loaded: $categories")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to load categories", e)
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

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            try {
                val task = useCases.getTaskById(id)
                _state.update { it.copy(selectedTask = task) }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to get task by ID", e)
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
                        _state.update { it.copy(tasks = filtered) }
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
                        _state.update { it.copy(tasks = filtered) }
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
                        _state.update { it.copy(tasks = filtered) }
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
        loadCategories()
    }
}