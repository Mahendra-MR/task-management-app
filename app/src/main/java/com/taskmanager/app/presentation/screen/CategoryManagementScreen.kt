package com.taskmanager.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskmanager.app.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    viewModel: TaskViewModel,
    onBack: () -> Unit,
    onViewTasksForCategory: (String) -> Unit // ✅ NEW: handle category click to view tasks
) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var newCategoryName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Manage Categories") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        newCategoryName = ""
                        showAddDialog = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Category")
                }
            }
        )

        // Content
        if (state.categories.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Category, contentDescription = null, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("No Categories Yet", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Add your first category to organize your tasks", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    newCategoryName = ""
                    showAddDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Category")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Your Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(state.categories) { category ->
                    CategoryItem(
                        category = category,
                        onClick = { onViewTasksForCategory(category) }, // ✅ navigate on click
                        onEdit = {
                            selectedCategory = category
                            newCategoryName = category
                            showEditDialog = true
                        },
                        onDelete = {
                            selectedCategory = category
                            showDeleteDialog = true
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            newCategoryName = ""
                            showAddDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Category")
                    }
                }
            }
        }
    }

    // Dialogs
    if (showAddDialog) {
        AddEditCategoryDialog(
            title = "Add Category",
            categoryName = newCategoryName,
            onCategoryNameChange = { newCategoryName = it },
            onConfirm = {
                if (newCategoryName.trim().isNotBlank()) {
                    viewModel.addCategory(newCategoryName.trim())
                    showAddDialog = false
                }
            },
            onDismiss = { showAddDialog = false }
        )
    }

    if (showEditDialog) {
        AddEditCategoryDialog(
            title = "Edit Category",
            categoryName = newCategoryName,
            onCategoryNameChange = { newCategoryName = it },
            onConfirm = {
                if (newCategoryName.trim().isNotBlank() && newCategoryName != selectedCategory) {
                    viewModel.updateCategory(selectedCategory, newCategoryName.trim())
                    showEditDialog = false
                }
            },
            onDismiss = { showEditDialog = false }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Category") },
            text = { Text("Are you sure you want to delete \"$selectedCategory\"? This will not delete its tasks.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCategory(selectedCategory)
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CategoryItem(
    category: String,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // ✅ Make entire card clickable
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun AddEditCategoryDialog(
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
            OutlinedTextField(
                value = categoryName,
                onValueChange = onCategoryNameChange,
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = categoryName.trim().isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
