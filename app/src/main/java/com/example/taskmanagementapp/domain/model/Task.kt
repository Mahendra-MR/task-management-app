package com.example.taskmanagementapp.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long,  // Timestamp in millis
    val priority: Priority,
    val category: String,
    val isCompleted: Boolean = false
)
