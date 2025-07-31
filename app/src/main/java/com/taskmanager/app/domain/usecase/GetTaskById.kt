package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.repository.TaskRepository

class GetTaskById(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int) = repository.getTaskById(id)
}
