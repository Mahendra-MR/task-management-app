package com.example.taskmanagementapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagementapp.domain.usecase.TaskUseCases

class TaskViewModelFactory(
    private val useCases: TaskUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(useCases) as T
    }
}
