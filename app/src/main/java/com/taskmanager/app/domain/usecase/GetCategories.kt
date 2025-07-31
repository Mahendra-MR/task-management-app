package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.repository.TaskRepository

class GetCategories(private val repository: TaskRepository) {
    suspend operator fun invoke(): List<String> = repository.getAllCategories()
}
