package com.example.taskmanagementapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapp.domain.model.Priority
import com.example.taskmanagementapp.presentation.components.PriorityDropdown
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel

@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    taskToEdit: Task? = null,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(taskToEdit?.description ?: "") }
    var category by remember { mutableStateOf(taskToEdit?.category ?: "") }
    var dueDate by remember { mutableStateOf(taskToEdit?.dueDate?.toString() ?: "") }
    var priority by remember { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIUM) }
    var isCompleted by remember { mutableStateOf(taskToEdit?.isCompleted ?: false) }

    var showValidationError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = dueDate,
            onValueChange = { dueDate = it },
            label = { Text("Due Date (timestamp)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        PriorityDropdown(selected = priority, onSelected = { priority = it })

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isCompleted, onCheckedChange = { isCompleted = it })
            Text("Completed")
        }

        if (showValidationError) {
            Text("Title, Category, and Due Date are required", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (title.isBlank() || category.isBlank() || dueDate.isBlank()) {
                    showValidationError = true
                } else {
                    val task = Task(
                        id = taskToEdit?.id ?: 0,
                        title = title,
                        description = description,
                        dueDate = dueDate.toLongOrNull() ?: System.currentTimeMillis(),
                        priority = priority,
                        category = category,
                        isCompleted = isCompleted
                    )
                    if (taskToEdit != null) viewModel.updateTask(task) else viewModel.addTask(task)
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (taskToEdit != null) "Update Task" else "Add Task")
        }
    }
}
