package com.example.taskmanagementapp.domain.usecase

import com.example.taskmanagementapp.domain.repository.TaskRepository

class GetAllTasks(private val repository: TaskRepository) {
    operator fun invoke() = repository.getAllTasks()
}
