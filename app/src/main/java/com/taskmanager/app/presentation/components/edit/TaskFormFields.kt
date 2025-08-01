package com.taskmanager.app.presentation.components.edit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.taskmanager.app.domain.model.Priority
import com.taskmanager.app.presentation.components.CategoryDropdown
import com.taskmanager.app.presentation.components.PriorityDropdown
import java.util.Calendar

@Composable
fun TaskFormFields(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    onNavigateToCategories: () -> Unit,
    priority: Priority,
    onPriorityChange: (Priority) -> Unit,
    selectedDateText: String,
    selectedTimeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    selectedDate: Calendar?,
    selectedTime: Calendar?,
    showValidationError: Boolean,
    categories: List<String>
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        label = { Text("Task Title *") },
        modifier = Modifier.fillMaxWidth(),
        isError = showValidationError && title.isBlank(),
        supportingText = if (showValidationError && title.isBlank()) {
            { Text("Title is required") }
        } else null
    )

    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("Description") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 3
    )

    CategoryDropdown(
        categories = categories,
        selectedCategory = category,
        onCategorySelected = onCategoryChange,
        onAddCategoryClick = onNavigateToCategories,
        modifier = Modifier.fillMaxWidth(),
        isError = showValidationError && category.isBlank(),
        supportingText = if (showValidationError && category.isBlank()) {
            { Text("Category is required") }
        } else null
    )

    OutlinedTextField(
        value = selectedDateText,
        onValueChange = { onDateClick() },
        label = { Text("Due Date *") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = onDateClick) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
            }
        },
        interactionSource = remember { MutableInteractionSource() }.apply {
            LaunchedEffect(this) {
                interactions.collect {
                    if (it is PressInteraction.Press) onDateClick()
                }
            }
        },
        isError = showValidationError && selectedDate == null,
        supportingText = if (showValidationError && selectedDate == null) {
            { Text("Due date is required") }
        } else null
    )

    OutlinedTextField(
        value = selectedTimeText,
        onValueChange = { onTimeClick() },
        label = { Text("Due Time *") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = onTimeClick) {
                Icon(Icons.Default.AccessTime, contentDescription = "Select Time")
            }
        },
        interactionSource = remember { MutableInteractionSource() }.apply {
            LaunchedEffect(this) {
                interactions.collect {
                    if (it is PressInteraction.Press) onTimeClick()
                }
            }
        },
        isError = showValidationError && selectedTime == null,
        supportingText = if (showValidationError && selectedTime == null) {
            { Text("Due time is required") }
        } else null
    )

    PriorityDropdown(
        selected = priority,
        onSelected = onPriorityChange,
        modifier = Modifier.fillMaxWidth()
    )
}
