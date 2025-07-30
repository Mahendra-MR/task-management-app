package com.example.taskmanagementapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.taskmanagementapp.data.local.dao.CategoryDao
import com.example.taskmanagementapp.data.local.dao.TaskDao
import com.example.taskmanagementapp.data.local.entities.CategoryEntity
import com.example.taskmanagementapp.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, CategoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
}

// Migration from version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create categories table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `categories` (
                `name` TEXT NOT NULL,
                PRIMARY KEY(`name`)
            )
        """.trimIndent())
    }
}