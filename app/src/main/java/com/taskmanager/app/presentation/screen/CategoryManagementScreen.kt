package com.taskmanager.app.presentation.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.vector.ImageVector
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.taskmanager.app.presentation.components.category.CategoryItem
import com.taskmanager.app.presentation.components.category.AddEditCategoryDialog
import com.taskmanager.app.presentation.components.category.CategoryEmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    viewModel: TaskViewModel,
    onBack: () -> Unit,
    onViewTasksForCategory: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var newCategoryName by remember { mutableStateOf("") }

    // ✅ Phase 3: derivedStateOf for empty list check
    val isCategoryListEmpty by remember(state.categories) {
        derivedStateOf { state.categories.isEmpty() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Manage Categories") },
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.refreshData()
                    onBack()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {
                    newCategoryName = ""
                    showAddDialog = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Category")
                }
            }
        )

        if (isCategoryListEmpty) {
            CategoryEmptyState { showAddDialog = true }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    CategoryHeader(state.categories.size)
                }

                items(state.categories, key = { it }) { category ->
                    CategoryItem(
                        category = category,
                        onClick = { onViewTasksForCategory(category) },
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
                    AddNewCategoryButton {
                        newCategoryName = ""
                        showAddDialog = true
                    }
                }
            }
        }
    }

    // 💬 Add Category Dialog
    if (showAddDialog) {
        AddEditCategoryDialog(
            title = "Add Category",
            categoryName = newCategoryName,
            onCategoryNameChange = { newCategoryName = it },
            onConfirm = {
                if (newCategoryName.trim().isNotBlank()) {
                    viewModel.addCategory(newCategoryName.trim())
                    showAddDialog = false
                    newCategoryName = ""
                }
            },
            onDismiss = {
                showAddDialog = false
                newCategoryName = ""
            }
        )
    }

    // 📝 Edit Category Dialog
    if (showEditDialog) {
        AddEditCategoryDialog(
            title = "Edit Category",
            categoryName = newCategoryName,
            onCategoryNameChange = { newCategoryName = it },
            onConfirm = {
                if (newCategoryName.trim().isNotBlank() && newCategoryName.trim() != selectedCategory) {
                    viewModel.updateCategory(selectedCategory, newCategoryName.trim())
                    showEditDialog = false
                    newCategoryName = ""
                }
            },
            onDismiss = {
                showEditDialog = false
                newCategoryName = ""
            }
        )
    }

    // 🗑️ Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Category") },
            text = { Text("Are you sure you want to delete \"$selectedCategory\"?") },
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
private fun CategoryHeader(count: Int) {
    Text(
        text = "Your Categories ($count)",
        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun AddNewCategoryButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Add New Category")
    }
}
