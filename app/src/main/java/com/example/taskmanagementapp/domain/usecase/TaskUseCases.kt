package com.example.taskmanagementapp.domain.usecase

data class TaskUseCases(
    val addTask: AddTask,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask,
    val getAllTasks: GetAllTasks,
    val getTaskById: GetTaskById,
    val getQuote: GetQuote,
    val getCategories: GetCategories,
    val filterTasks: FilterTasks
)
