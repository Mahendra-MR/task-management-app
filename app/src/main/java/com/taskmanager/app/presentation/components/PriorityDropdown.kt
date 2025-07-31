package com.taskmanager.app.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.taskmanager.app.domain.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(
    selected: Priority,
    onSelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.name,
            onValueChange = { },
            readOnly = true,
            label = { Text("Priority") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.values().forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        onSelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}