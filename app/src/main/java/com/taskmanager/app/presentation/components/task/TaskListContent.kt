package com.taskmanager.app.presentation.components.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskmanager.app.domain.model.Task

@Composable
fun TaskListContent(
    tasks: List<Task>,
    hasFilters: Boolean,
    onTaskClick: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onToggleStatus: (Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!hasFilters) {
            val groupedByStatus = listOf(
                "Pending" to tasks.filter { !it.isCompleted },
                "Completed" to tasks.filter { it.isCompleted }
            )

            groupedByStatus.forEach { (label, groupedTasks) ->
                if (groupedTasks.isNotEmpty()) {
                    item {
                        Text(
                            text = "$label (${groupedTasks.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(groupedTasks) { task ->
                        EnhancedTaskCard(
                            task = task,
                            onClick = { onTaskClick(task) },
                            onEdit = { onEditTask(task) },
                            onStatusToggle = {
                                onToggleStatus(it.copy(isCompleted = !it.isCompleted))
                            }
                        )
                    }
                }
            }
        } else {
            val groupedByCategory = tasks.groupBy { it.category }

            groupedByCategory.forEach { (category, groupedTasks) ->
                item {
                    Text(
                        text = "$category (${groupedTasks.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                items(groupedTasks) { task ->
                    EnhancedTaskCard(
                        task = task,
                        onClick = { onTaskClick(task) },
                        onEdit = { onEditTask(task) },
                        onStatusToggle = {
                            onToggleStatus(it.copy(isCompleted = !it.isCompleted))
                        }
                    )
                }
            }
        }
    }
}
