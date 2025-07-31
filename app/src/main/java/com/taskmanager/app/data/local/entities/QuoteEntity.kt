package com.taskmanager.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1, // always only one cached quote
    val content: String,
    val author: String
)
