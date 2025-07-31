package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.domain.repository.TaskRepository

class AddTask(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.addTask(task)
}
