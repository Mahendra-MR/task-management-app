package com.taskmanager.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.taskmanager.app.data.local.dao.CategoryDao
import com.taskmanager.app.data.local.dao.QuoteDao
import com.taskmanager.app.data.local.dao.TaskDao
import com.taskmanager.app.data.local.entities.CategoryEntity
import com.taskmanager.app.data.local.entities.QuoteEntity
import com.taskmanager.app.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, CategoryEntity::class, QuoteEntity::class],
    version = 3,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun quoteDao(): QuoteDao
}

// Migration from version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `categories` (
                `name` TEXT NOT NULL,
                PRIMARY KEY(`name`)
            )
        """.trimIndent())
    }
}

// Migration from version 2 to 3 (for QuoteEntity)
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `quotes` (
                `id` INTEGER NOT NULL PRIMARY KEY,
                `content` TEXT NOT NULL,
                `author` TEXT NOT NULL
            )
        """.trimIndent())
    }
}