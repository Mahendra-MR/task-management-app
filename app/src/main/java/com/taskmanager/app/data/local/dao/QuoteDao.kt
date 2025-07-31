package com.taskmanager.app.data.local.dao

import androidx.room.*
import com.taskmanager.app.data.local.entities.QuoteEntity

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("SELECT * FROM quotes WHERE id = 1")
    suspend fun getCachedQuote(): QuoteEntity?
}
