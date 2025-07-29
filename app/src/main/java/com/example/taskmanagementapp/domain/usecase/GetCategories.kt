package com.example.taskmanagementapp.domain.usecase

import com.example.taskmanagementapp.domain.repository.TaskRepository

class GetCategories(private val repository: TaskRepository) {
    suspend operator fun invoke(): List<String> = repository.getAllCategories()
}
