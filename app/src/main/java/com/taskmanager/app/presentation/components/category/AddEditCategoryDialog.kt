package com.taskmanager.app.presentation.components.category

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*

@Composable
fun AddEditCategoryDialog(
    title: String,
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = onCategoryNameChange,
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("Category names should be descriptive and unique") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = categoryName.trim().isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
