package com.taskmanager.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.taskmanager.app.domain.model.Task
import androidx.compose.ui.Alignment

@Composable
fun HighPriorityTasksSection(
    tasks: List<Task>,
    onViewAllClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (tasks.isNotEmpty()) {
                Text(
                    "High Priority Tasks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // ✅ LazyColumn for better performance even if short list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    userScrollEnabled = false
                ) {
                    items(tasks.take(3)) { task ->
                        Text("• ${task.title}", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                TextButton(
                    onClick = onViewAllClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("View All")
                }
            } else {
                Text(
                    "No high-priority pending tasks.",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "Create a new task or explore your existing ones.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
