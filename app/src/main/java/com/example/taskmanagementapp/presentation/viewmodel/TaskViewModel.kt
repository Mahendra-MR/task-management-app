package com.example.taskmanagementapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.domain.model.Quote
import com.example.taskmanagementapp.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
                val quote = useCases.getQuote()
                _state.update { it.copy(quote = quote) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to load quote.") }
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
}
