package com.example.taskmanagementapp.domain.usecase

import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.domain.repository.TaskRepository

class DeleteTask(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.deleteTask(task)
}
