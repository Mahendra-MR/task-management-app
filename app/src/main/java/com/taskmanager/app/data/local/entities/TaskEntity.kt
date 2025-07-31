
package com.taskmanager.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: String,   // Stored as string, mapped from enum
    val category: String,
    val isCompleted: Boolean = false
)