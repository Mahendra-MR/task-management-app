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
    pendingTasks: List<Task>,
    completedTasks: List<Task>,
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
            if (pendingTasks.isNotEmpty()) {
                item {
                    SectionHeader("Pending (${pendingTasks.size})")
                }
                items(pendingTasks, key = { it.id }) { task ->
                    EnhancedTaskCard(
                        task = task,
                        onClick = { onTaskClick(task) },
                        onEdit = { onEditTask(task) },
                        onStatusToggle = { onToggleStatus(it.copy(isCompleted = true)) }
                    )
                }
            }

            if (completedTasks.isNotEmpty()) {
                item {
                    SectionHeader("Completed (${completedTasks.size})")
                }
                items(completedTasks, key = { it.id }) { task ->
                    EnhancedTaskCard(
                        task = task,
                        onClick = { onTaskClick(task) },
                        onEdit = { onEditTask(task) },
                        onStatusToggle = { onToggleStatus(it.copy(isCompleted = false)) }
                    )
                }
            }
        } else {
            val groupedByCategory = (pendingTasks + completedTasks)
                .groupBy { it.category }

            groupedByCategory.forEach { (category, groupedTasks) ->
                item {
                    SectionHeader("$category (${groupedTasks.size})")
                }
                items(groupedTasks, key = { it.id }) { task ->
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

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}
