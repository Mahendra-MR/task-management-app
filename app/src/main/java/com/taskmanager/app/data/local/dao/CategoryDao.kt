package com.taskmanager.app.data.local.dao

import androidx.room.*
import com.taskmanager.app.data.local.entities.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("DELETE FROM categories WHERE name = :categoryName")
    suspend fun deleteCategoryByName(categoryName: String)
}