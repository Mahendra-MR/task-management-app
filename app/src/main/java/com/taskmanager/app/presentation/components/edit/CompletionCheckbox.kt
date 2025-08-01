package com.taskmanager.app.presentation.components.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CompletionCheckbox(isCompleted: Boolean, onCheckChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isCompleted, onCheckedChange = onCheckChange)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Mark as completed")
    }
}
