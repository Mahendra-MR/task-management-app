package com.example.taskmanagementapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapp.domain.model.Priority

data class FilterState(
    val selectedCategory: String? = null,
    val selectedPriority: Priority? = null,
    val selectedStatus: Boolean? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBar(
    categories: List<String>,
    filterState: FilterState,
    onFilterChange: (FilterState) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    val hasActiveFilters = filterState.selectedCategory != null ||
            filterState.selectedPriority != null ||
            filterState.selectedStatus != null

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (hasActiveFilters)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = if (hasActiveFilters)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasActiveFilters) "Filters Applied" else "Filter Tasks",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (hasActiveFilters) FontWeight.Medium else FontWeight.Normal,
                    color = if (hasActiveFilters)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (hasActiveFilters) {
                    TextButton(
                        onClick = onClearFilters,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear")
                    }
                }

                OutlinedButton(
                    onClick = { showFilterDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (hasActiveFilters)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Filter")
                }
            }
        }

        // Active filters display
        if (hasActiveFilters) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterState.selectedCategory?.let { category ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                onFilterChange(filterState.copy(selectedCategory = null))
                            },
                            label = { Text("Category: $category") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Remove filter",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        )
                    }

                    filterState.selectedPriority?.let { priority ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                onFilterChange(filterState.copy(selectedPriority = null))
                            },
                            label = { Text("Priority: ${priority.name}") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Remove filter",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        )
                    }

                    filterState.selectedStatus?.let { status ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                onFilterChange(filterState.copy(selectedStatus = null))
                            },
                            label = { Text("Status: ${if (status) "Completed" else "Pending"}") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Remove filter",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            categories = categories,
            filterState = filterState,
            onFilterChange = onFilterChange,
            onDismiss = { showFilterDialog = false }
        )
    }
}

@Composable
private fun FilterDialog(
    categories: List<String>,
    filterState: FilterState,
    onFilterChange: (FilterState) -> Unit,
    onDismiss: () -> Unit
) {
    var tempFilterState by remember { mutableStateOf(filterState) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Tasks") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category Filter
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = tempFilterState.selectedCategory == null,
                        onClick = {
                            tempFilterState = tempFilterState.copy(selectedCategory = null)
                        },
                        label = { Text("All") }
                    )

                    categories.forEach { category ->
                        FilterChip(
                            selected = tempFilterState.selectedCategory == category,
                            onClick = {
                                tempFilterState = tempFilterState.copy(
                                    selectedCategory = if (tempFilterState.selectedCategory == category) null else category
                                )
                            },
                            label = { Text(category) }
                        )
                    }
                }

                // Priority Filter
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = tempFilterState.selectedPriority == null,
                        onClick = {
                            tempFilterState = tempFilterState.copy(selectedPriority = null)
                        },
                        label = { Text("All") }
                    )

                    Priority.values().forEach { priority ->
                        FilterChip(
                            selected = tempFilterState.selectedPriority == priority,
                            onClick = {
                                tempFilterState = tempFilterState.copy(
                                    selectedPriority = if (tempFilterState.selectedPriority == priority) null else priority
                                )
                            },
                            label = { Text(priority.name) }
                        )
                    }
                }

                // Status Filter
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = tempFilterState.selectedStatus == null,
                        onClick = {
                            tempFilterState = tempFilterState.copy(selectedStatus = null)
                        },
                        label = { Text("All") }
                    )

                    FilterChip(
                        selected = tempFilterState.selectedStatus == false,
                        onClick = {
                            tempFilterState = tempFilterState.copy(
                                selectedStatus = if (tempFilterState.selectedStatus == false) null else false
                            )
                        },
                        label = { Text("Pending") }
                    )

                    FilterChip(
                        selected = tempFilterState.selectedStatus == true,
                        onClick = {
                            tempFilterState = tempFilterState.copy(
                                selectedStatus = if (tempFilterState.selectedStatus == true) null else true
                            )
                        },
                        label = { Text("Completed") }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onFilterChange(tempFilterState)
                    onDismiss()
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}