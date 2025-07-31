package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.domain.repository.TaskRepository

class UpdateTask(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}
