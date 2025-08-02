package com.taskmanager.app.presentation.components.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskmanager.app.domain.model.Task
import java.text.SimpleDateFormat
import androidx.compose.runtime.remember
import java.util.Date
import java.util.Locale

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

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

            TaskMetaRow(label = "Due", value = dateFormat.format(Date(task.dueDate)))
            TaskMetaRow(label = "Priority", value = task.priority.name)
            TaskMetaRow(label = "Category", value = task.category)
            TaskMetaRow(label = "Status", value = if (task.isCompleted) "Completed" else "Pending")
        }
    }
}

@Composable
private fun TaskMetaRow(label: String, value: String) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.labelSmall
    )
}
