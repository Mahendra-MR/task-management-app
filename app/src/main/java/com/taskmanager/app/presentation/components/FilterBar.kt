package com.taskmanager.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.taskmanager.app.domain.model.Priority

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
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .heightIn(max = 80.dp) // Limit maximum height
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    filterState.selectedCategory?.let { category ->
                        item {
                            FilterChip(
                                selected = true,
                                onClick = {
                                    onFilterChange(filterState.copy(selectedCategory = null))
                                },
                                label = { Text("Category: $category", maxLines = 1) },
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

                    filterState.selectedPriority?.let { priority ->
                        item {
                            FilterChip(
                                selected = true,
                                onClick = {
                                    onFilterChange(filterState.copy(selectedPriority = null))
                                },
                                label = { Text("Priority: ${priority.name}", maxLines = 1) },
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

                    filterState.selectedStatus?.let { status ->
                        item {
                            FilterChip(
                                selected = true,
                                onClick = {
                                    onFilterChange(filterState.copy(selectedStatus = null))
                                },
                                label = { Text("Status: ${if (status) "Completed" else "Pending"}", maxLines = 1) },
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
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Allow dialog to use more screen space
        ),
        modifier = Modifier
            .fillMaxWidth(0.95f) // Use 95% of screen width
            .wrapContentHeight(),
        title = {
            Text(
                "Filter Tasks",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp) // Limit maximum height
                    .verticalScroll(rememberScrollState()), // Make it scrollable
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Category Filter Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Category chips in a flow layout
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // "All" option
                        Row {
                            FilterChip(
                                selected = tempFilterState.selectedCategory == null,
                                onClick = {
                                    tempFilterState = tempFilterState.copy(selectedCategory = null)
                                },
                                label = { Text("All Categories") }
                            )
                        }

                        // Category options - group them in rows of 2
                        categories.chunked(2).forEach { rowCategories ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowCategories.forEach { category ->
                                    FilterChip(
                                        selected = tempFilterState.selectedCategory == category,
                                        onClick = {
                                            tempFilterState = tempFilterState.copy(
                                                selectedCategory = if (tempFilterState.selectedCategory == category) null else category
                                            )
                                        },
                                        label = { Text(category) },
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                }
                                // Fill remaining space if odd number of categories
                                if (rowCategories.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Divider()

                // Priority Filter Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Priority",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // "All" option
                        Row {
                            FilterChip(
                                selected = tempFilterState.selectedPriority == null,
                                onClick = {
                                    tempFilterState = tempFilterState.copy(selectedPriority = null)
                                },
                                label = { Text("All Priorities") }
                            )
                        }

                        // Priority options in a single row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Priority.values().forEach { priority ->
                                FilterChip(
                                    selected = tempFilterState.selectedPriority == priority,
                                    onClick = {
                                        tempFilterState = tempFilterState.copy(
                                            selectedPriority = if (tempFilterState.selectedPriority == priority) null else priority
                                        )
                                    },
                                    label = { Text(priority.name) },
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                            }
                        }
                    }
                }

                Divider()

                // Status Filter Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // "All" option
                        Row {
                            FilterChip(
                                selected = tempFilterState.selectedStatus == null,
                                onClick = {
                                    tempFilterState = tempFilterState.copy(selectedStatus = null)
                                },
                                label = { Text("All Tasks") }
                            )
                        }

                        // Status options
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = tempFilterState.selectedStatus == false,
                                onClick = {
                                    tempFilterState = tempFilterState.copy(
                                        selectedStatus = if (tempFilterState.selectedStatus == false) null else false
                                    )
                                },
                                label = { Text("Pending") },
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            FilterChip(
                                selected = tempFilterState.selectedStatus == true,
                                onClick = {
                                    tempFilterState = tempFilterState.copy(
                                        selectedStatus = if (tempFilterState.selectedStatus == true) null else true
                                    )
                                },
                                label = { Text("Completed") },
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onFilterChange(tempFilterState)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(0.4f)
            ) {
                Text("Apply Filters")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(0.4f)
            ) {
                Text("Cancel")
            }
        }
    )
}