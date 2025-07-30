package com.example.taskmanagementapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.presentation.components.FilterBar
import com.example.taskmanagementapp.presentation.components.FilterState
import com.example.taskmanagementapp.presentation.components.TaskCard
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onTaskClick: (Task) -> Unit,
    onEditTask: (Task) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var filterState by remember { mutableStateOf(FilterState()) }

    // Apply filters to tasks
    val filteredTasks = remember(state.tasks, filterState) {
        state.tasks.filter { task ->
            val categoryMatch = filterState.selectedCategory?.let {
                task.category == it
            } ?: true

            val priorityMatch = filterState.selectedPriority?.let {
                task.priority == it
            } ?: true

            val statusMatch = filterState.selectedStatus?.let {
                task.isCompleted == it
            } ?: true

            categoryMatch && priorityMatch && statusMatch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Filter Bar
        FilterBar(
            categories = state.categories,
            filterState = filterState,
            onFilterChange = { filterState = it },
            onClearFilters = { filterState = FilterState() },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (filteredTasks.isEmpty()) {
            if (state.tasks.isEmpty()) {
                EmptyTaskState()
            } else {
                EmptyFilterState(
                    onClearFilters = { filterState = FilterState() }
                )
            }
        } else {
            val groupedTasks = filteredTasks.groupBy { it.category }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                groupedTasks.forEach { (category, tasksInCategory) ->
                    item {
                        Text(
                            text = "$category (${tasksInCategory.size})",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 8.dp)
                                .fillMaxWidth()
                        )
                    }

                    items(tasksInCategory) { task ->
                        EnhancedTaskCard(
                            task = task,
                            onClick = { onTaskClick(task) },
                            onEdit = { onEditTask(task) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedTaskCard(
    task: Task,
    onClick: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface
                    )

                    if (task.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (task.isCompleted)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Task",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Task Details Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority Badge
                Surface(
                    color = when (task.priority.name) {
                        "HIGH" -> Color.Red.copy(alpha = 0.1f)
                        "MEDIUM" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                        else -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = task.priority.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = when (task.priority.name) {
                            "HIGH" -> Color.Red
                            "MEDIUM" -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        },
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Status Badge
                Surface(
                    color = if (task.isCompleted)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = if (task.isCompleted) "Completed" else "Pending",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clickable area for task details
            TextButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "View Details",
                    style = MaterialTheme.typography.labelMedium
                )
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

@Composable
fun EmptyFilterState(
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "No Filtered Tasks",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No tasks match your filters",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Try adjusting your filter criteria",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClearFilters
        ) {
            Text("Clear Filters")
        }
    }
}