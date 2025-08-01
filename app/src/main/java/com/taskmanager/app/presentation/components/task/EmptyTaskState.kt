package com.taskmanager.app.presentation.components.task

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EmptyTaskState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "No Tasks",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No tasks available",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Try adding a task using the home screen!",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
    }
}
