package com.example.taskmanagementapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapp.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

            Text(
                text = "Due: ${dateFormat.format(Date(task.dueDate))}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Priority: ${task.priority}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Category: ${task.category}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Status: ${if (task.isCompleted) "Completed" else "Pending"}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
