package com.taskmanager.app.domain.usecase

import com.taskmanager.app.domain.repository.TaskRepository

class AddCategory(private val repository: TaskRepository) {
    suspend operator fun invoke(category: String) = repository.addCategory(category)
}

class DeleteCategory(private val repository: TaskRepository) {
    suspend operator fun invoke(category: String) = repository.deleteCategory(category)
}

class UpdateCategory(private val repository: TaskRepository) {
    suspend operator fun invoke(oldCategory: String, newCategory: String) =
        repository.updateCategory(oldCategory, newCategory)
}