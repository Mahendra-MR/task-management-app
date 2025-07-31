package com.taskmanager.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taskmanager.app.domain.usecase.TaskUseCases

class TaskViewModelFactory(
    private val useCases: TaskUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(useCases) as T
    }
}
