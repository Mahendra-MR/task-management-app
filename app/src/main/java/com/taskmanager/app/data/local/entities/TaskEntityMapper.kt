package com.taskmanager.app.data.local.entities

import com.taskmanager.app.domain.model.Priority
import com.taskmanager.app.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        priority = Priority.valueOf(priority),
        category = category,
        isCompleted = isCompleted
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        priority = priority.name,
        category = category,
        isCompleted = isCompleted
    )
}