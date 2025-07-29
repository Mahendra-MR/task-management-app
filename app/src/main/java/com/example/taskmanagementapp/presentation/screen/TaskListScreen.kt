package com.example.taskmanagementapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel
import com.example.taskmanagementapp.presentation.components.TaskCard
import com.example.taskmanagementapp.domain.model.Task

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onTaskClick: (Task) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("All Tasks", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        if (state.tasks.isEmpty()) {
            Text("No tasks found.")
        } else {
            LazyColumn {
                items(state.tasks) { task ->
                    TaskCard(task = task, onClick = { onTaskClick(task) })
                }
            }
        }
    }
}
