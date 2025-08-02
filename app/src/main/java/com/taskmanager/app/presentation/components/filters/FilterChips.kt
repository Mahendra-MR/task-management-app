package com.taskmanager.app.presentation.components.filters

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskmanager.app.domain.model.Priority

@Composable
fun FilterCategorySection(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Category", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

        Row {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All Categories") }
            )
        }

        categories.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            onCategorySelected(
                                if (selectedCategory == category) null else category
                            )
                        },
                        label = { Text(category) },
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun FilterPrioritySection(
    selectedPriority: Priority?,
    onPrioritySelected: (Priority?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Priority", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

        Row {
            FilterChip(
                selected = selectedPriority == null,
                onClick = { onPrioritySelected(null) },
                label = { Text("All Priorities") }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Priority.values().forEach { priority ->
                FilterChip(
                    selected = selectedPriority == priority,
                    onClick = {
                        onPrioritySelected(
                            if (selectedPriority == priority) null else priority
                        )
                    },
                    label = { Text(priority.name) },
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }
    }
}

@Composable
fun FilterStatusSection(
    selectedStatus: Boolean?,
    onStatusSelected: (Boolean?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Status", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onStatusSelected(null) },
                label = { Text("All Tasks") },
                modifier = Modifier.weight(1f, fill = false)
            )
            FilterChip(
                selected = selectedStatus == false,
                onClick = {
                    onStatusSelected(if (selectedStatus == false) null else false)
                },
                label = { Text("Pending") },
                modifier = Modifier.weight(1f, fill = false)
            )
            FilterChip(
                selected = selectedStatus == true,
                onClick = {
                    onStatusSelected(if (selectedStatus == true) null else true)
                },
                label = { Text("Completed") },
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}
