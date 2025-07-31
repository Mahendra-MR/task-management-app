package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.repository.TaskRepository

class GetQuote(private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.getRandomQuote()
}
