package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.repository.TaskRepository

class GetAllTasks(private val repository: TaskRepository) {
    operator fun invoke() = repository.getAllTasks()
}
