package com.taskmanager.app.presentation.components.filters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.taskmanager.app.domain.model.Priority

@Composable
fun FilterDialog(
    categories: List<String>,
    filterState: FilterState,
    onFilterChange: (FilterState) -> Unit,
    onDismiss: () -> Unit
) {
    var tempFilterState by remember { mutableStateOf(filterState) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onFilterChange(tempFilterState)
            }) {
                Text("Apply")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                text = "Filter Tasks",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section for selecting category
                FilterCategorySection(
                    categories = categories,
                    selectedCategory = tempFilterState.selectedCategory,
                    onCategorySelected = { selected ->
                        tempFilterState = tempFilterState.copy(selectedCategory = selected)
                    }
                )

                // Section for selecting priority
                FilterPrioritySection(
                    selectedPriority = tempFilterState.selectedPriority,
                    onPrioritySelected = { selected ->
                        tempFilterState = tempFilterState.copy(selectedPriority = selected)
                    }
                )

                // Section for selecting completion status
                FilterStatusSection(
                    selectedStatus = tempFilterState.selectedStatus,
                    onStatusSelected = { selected ->
                        tempFilterState = tempFilterState.copy(selectedStatus = selected)
                    }
                )
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.95f)
    )
}
