package com.example.taskmanagementapp.domain.usecase

import com.example.taskmanagementapp.domain.repository.TaskRepository

class GetTaskById(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int) = repository.getTaskById(id)
}
