package com.taskmanager.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.domain.model.Quote
import com.taskmanager.app.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.io.IOException

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
        loadTasks()
        loadQuote()
        loadCategories()
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

    fun loadQuote() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                Log.d("TaskViewModel", "Starting to load quote...")

                val quote = useCases.getQuote()
                Log.d("TaskViewModel", "Quote loaded successfully: ${quote.content}")

                _state.update { it.copy(quote = quote, isLoading = false, error = null) }
            } catch (e: UnknownHostException) {
                Log.e("TaskViewModel", "No internet", e)
                _state.update { it.copy(error = "No internet connection", isLoading = false) }
            } catch (e: IOException) {
                Log.e("TaskViewModel", "Network error", e)
                _state.update { it.copy(error = "Network error. Try again.", isLoading = false) }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Unexpected error", e)
                _state.update { it.copy(error = "Failed to load quote: ${e.message}", isLoading = false) }
            }
        }
    }

    fun retryLoadQuote() {
        loadQuote()
    }

    fun loadCategories() {
        viewModelScope.launch {
            val categories = useCases.getCategories()
            _state.update { it.copy(categories = categories) }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            useCases.addTask(task)
            loadTasks()
            loadCategories()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            useCases.updateTask(task)
            loadTasks()
            loadCategories()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            useCases.deleteTask(task)
            loadTasks()
        }
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            val task = useCases.getTaskById(id)
            _state.update { it.copy(selectedTask = task) }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            useCases.filterTasks.byCategory(category)
                .distinctUntilChanged()
                .collect { filtered ->
                    _tasks.value = filtered
                    _state.update { it.copy(tasks = filtered) }
                }
        }
    }

    fun filterByPriority(priority: String) {
        viewModelScope.launch {
            useCases.filterTasks.byPriority(priority)
                .distinctUntilChanged()
                .collect { filtered ->
                    _tasks.value = filtered
                    _state.update { it.copy(tasks = filtered) }
                }
        }
    }

    fun filterByStatus(isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.filterTasks.byStatus(isCompleted)
                .distinctUntilChanged()
                .collect { filtered ->
                    _tasks.value = filtered
                    _state.update { it.copy(tasks = filtered) }
                }
        }
    }

    // Category management methods
    fun addCategory(category: String) {
        viewModelScope.launch {
            try {
                useCases.addCategory(category)
                loadCategories()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to add category: ${e.message}", e)
            }
        }
    }

    fun deleteCategory(category: String) {
        viewModelScope.launch {
            try {
                useCases.deleteCategory(category)
                loadCategories()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to delete category: ${e.message}", e)
            }
        }
    }

    fun updateCategory(oldCategory: String, newCategory: String) {
        viewModelScope.launch {
            try {
                useCases.updateCategory(oldCategory, newCategory)
                loadCategories()
                loadTasks()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to update category: ${e.message}", e)
            }
        }
    }
}
