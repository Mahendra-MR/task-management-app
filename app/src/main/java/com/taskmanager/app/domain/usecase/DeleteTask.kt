package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.domain.repository.TaskRepository

class DeleteTask(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.deleteTask(task)
}
