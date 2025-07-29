package com.example.taskmanagementapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.domain.model.Quote
import com.example.taskmanagementapp.domain.usecase.TaskUseCases
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

    init {
        loadTasks()
        loadQuote()
        loadCategories()
    }

    fun loadTasks() {
        viewModelScope.launch {
            useCases.getAllTasks().collect { tasks ->
                _state.update { it.copy(tasks = tasks) }
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
                Log.e("TaskViewModel", "Network error: No internet connection", e)
                _state.update { it.copy(
                    error = "No internet connection. Please check your network.",
                    isLoading = false
                ) }
            } catch (e: IOException) {
                Log.e("TaskViewModel", "Network error: ${e.message}", e)
                _state.update { it.copy(
                    error = "Network error. Please try again later.",
                    isLoading = false
                ) }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to load quote: ${e.message}", e)
                _state.update { it.copy(
                    error = "Failed to load quote: ${e.message}",
                    isLoading = false
                ) }
            }
        }
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
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            useCases.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            useCases.deleteTask(task)
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
            useCases.filterTasks.byCategory(category).collect {
                _state.update { state -> state.copy(tasks = it) }
            }
        }
    }

    fun filterByPriority(priority: String) {
        viewModelScope.launch {
            useCases.filterTasks.byPriority(priority).collect {
                _state.update { state -> state.copy(tasks = it) }
            }
        }
    }

    fun filterByStatus(isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.filterTasks.byStatus(isCompleted).collect {
                _state.update { state -> state.copy(tasks = it) }
            }
        }
    }

    fun retryLoadQuote() {
        loadQuote()
    }
}