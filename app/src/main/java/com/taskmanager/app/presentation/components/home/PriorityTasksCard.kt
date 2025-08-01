package com.taskmanager.app.presentation.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.taskmanager.app.domain.model.Priority
import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.presentation.navigation.Routes
import androidx.compose.ui.Alignment


@Composable
fun PriorityTasksCard(tasks: List<Task>, navController: NavController) {
    val highPriorityTasks = tasks.filter { it.priority == Priority.HIGH && !it.isCompleted }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (highPriorityTasks.isNotEmpty()) {
                Text("High Priority Tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                highPriorityTasks.take(3).forEach { task ->
                    Text("• ${task.title}", style = MaterialTheme.typography.bodyMedium)
                }
                TextButton(
                    onClick = { navController.navigate(Routes.TASK_LIST) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("View All")
                }
            } else {
                Text("No high-priority pending tasks.", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Create a new task or explore your existing ones.", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
