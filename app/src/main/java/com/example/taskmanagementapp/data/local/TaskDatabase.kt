package com.example.taskmanagementapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanagementapp.data.local.dao.TaskDao
import com.example.taskmanagementapp.data.local.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
