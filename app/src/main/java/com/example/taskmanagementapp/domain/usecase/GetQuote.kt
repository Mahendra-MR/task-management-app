package com.example.taskmanagementapp.domain.usecase

import com.example.taskmanagementapp.domain.repository.TaskRepository

class GetQuote(private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.getRandomQuote()
}
