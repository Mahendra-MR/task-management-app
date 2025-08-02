package com.taskmanager.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.app.domain.model.*
import com.taskmanager.app.domain.usecase.TaskUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException
import java.util.*

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

    val tasks: StateFlow<List<Task>> = state.map { it.tasks }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = state.map { it.categories }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quote: StateFlow<Quote?> = state.map { it.quote }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isLoading: StateFlow<Boolean> = state.map { it.isLoading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val error: StateFlow<String?> = state.map { it.error }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedTask: StateFlow<Task?> = state.map { it.selectedTask }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val highPriorityTasks: StateFlow<List<Task>> = tasks.map { list ->
        list.filter { it.priority == Priority.HIGH && !it.isCompleted }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedTasks: StateFlow<List<Task>> = tasks.map { list ->
        list.filter { it.isCompleted }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dueTodayTasks: StateFlow<List<Task>> = tasks.map { list ->
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val todayEnd = todayStart + 86400000L

        list.filter { !it.isCompleted && it.dueDate in todayStart until todayEnd }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val overdueTasks: StateFlow<List<Task>> = tasks.map { list ->
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        list.filter { !it.isCompleted && it.dueDate < todayStart }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    data class HomeUiState(
        val quote: Quote? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val highPriorityPreviewTasks: List<Task> = emptyList()
    )

    val homeUiState: StateFlow<HomeUiState> = combine(
        quote, isLoading, error, highPriorityTasks
    ) { quote, isLoading, error, highPriorityTasks ->
        HomeUiState(
            quote = quote,
            isLoading = isLoading,
            error = error,
            highPriorityPreviewTasks = highPriorityTasks.take(3)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        loadTasks()
        loadCategories()
        loadQuote()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                useCases.getAllTasks()
                    .distinctUntilChanged()
                    .collect { taskList ->
                        _state.update { it.copy(tasks = taskList) }
                    }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to load tasks", e)
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = useCases.getCategories()
                _state.update { it.copy(categories = categories) }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to load categories", e)
            }
        }
    }

    fun loadQuote() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val quote = useCases.getQuote()
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

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                useCases.addTask(task)
                // no need to call loadTasks() if DB emits flow automatically
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to add task", e)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                useCases.updateTask(task)
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to update task", e)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                useCases.deleteTask(task)
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

    fun addCategory(category: String) {
        viewModelScope.launch {
            try {
                useCases.addCategory(category)
                loadCategories()
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
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to delete category", e)
            }
        }
    }

    fun updateCategory(oldCategory: String, newCategory: String) {
        viewModelScope.launch {
            try {
                useCases.updateCategory(oldCategory, newCategory)
                loadCategories()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to update category", e)
            }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            try {
                useCases.filterTasks.byCategory(category)
                    .distinctUntilChanged()
                    .collect { filtered ->
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
                        _state.update { it.copy(tasks = filtered) }
                    }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Failed to filter by status", e)
            }
        }
    }
}
