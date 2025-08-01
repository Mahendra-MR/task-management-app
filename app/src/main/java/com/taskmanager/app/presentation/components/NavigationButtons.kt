package com.taskmanager.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NavigationButtons(
    onTasksClick: () -> Unit,
    onNewTaskClick: () -> Unit,
    onCategoriesClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationButton("My Tasks", onTasksClick)
        NavigationButton("New Task", onNewTaskClick)
        NavigationButton("Categories", onCategoriesClick)
    }
}

@Composable
private fun NavigationButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(300.dp)
            .height(48.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}
