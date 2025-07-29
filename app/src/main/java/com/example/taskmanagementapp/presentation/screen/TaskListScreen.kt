package com.example.taskmanagementapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.presentation.components.TaskCard
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onTaskClick: (Task) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.tasks.isEmpty()) {
            EmptyTaskState()
        } else {
            val groupedTasks = state.tasks.groupBy { it.category }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                groupedTasks.forEach { (category, tasksInCategory) ->
                    item {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 8.dp)
                                .fillMaxWidth()
                        )
                    }

                    items(tasksInCategory) { task ->
                        TaskCard(task = task, onClick = { onTaskClick(task) })
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyTaskState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "No Tasks",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No tasks available",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Try adding a task using the home screen!",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
    }
}
