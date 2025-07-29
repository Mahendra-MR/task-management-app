package com.example.taskmanagementapp.domain.usecase

import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.domain.repository.TaskRepository

class UpdateTask(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}
