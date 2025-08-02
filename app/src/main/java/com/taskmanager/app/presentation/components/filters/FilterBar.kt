package com.taskmanager.app.presentation.components.filters

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
            Row(verticalAlignment = Alignment.CenterVertically) {
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (hasActiveFilters) {
                    TextButton(onClick = onClearFilters) {
                        Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear")
                    }
                }
                OutlinedButton(onClick = { showFilterDialog = true }) {
                    Text("Filter")
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            categories = categories,
            filterState = filterState,
            onFilterChange = {
                onFilterChange(it)
                showFilterDialog = false
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}
