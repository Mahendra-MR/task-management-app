package com.taskmanager.app.presentation.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.taskmanager.app.domain.model.Priority
import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.presentation.components.CategoryDropdown
import com.taskmanager.app.presentation.components.PriorityDropdown
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    taskToEdit: Task? = null,
    onSave: () -> Unit,
    onNavigateToCategories: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val categories by viewModel.categories.collectAsState()

    // Date and time formatters
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    // Form state
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(taskToEdit?.description ?: "") }
    var category by remember { mutableStateOf(taskToEdit?.category ?: "") }
    var priority by remember { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIUM) }
    var isCompleted by remember { mutableStateOf(taskToEdit?.isCompleted ?: false) }
    var showValidationError by remember { mutableStateOf(false) }

    // Date and time state
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    var selectedTime by remember { mutableStateOf<Calendar?>(null) }

    // Initialize with existing task data
    LaunchedEffect(taskToEdit) {
        if (taskToEdit != null && taskToEdit.dueDate > 0) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = taskToEdit.dueDate
            }
            selectedDate = calendar.clone() as Calendar
            selectedTime = calendar.clone() as Calendar
        }
    }

    // Load categories once
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    val selectedDateText = selectedDate?.let { dateFormat.format(it.time) } ?: ""
    val selectedTimeText = selectedTime?.let { timeFormat.format(it.time) } ?: ""
    val dueDateMillis = remember(selectedDate, selectedTime) {
        if (selectedDate != null && selectedTime != null) {
            val combined = (selectedDate!!.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, selectedTime!!.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, selectedTime!!.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            combined.timeInMillis
        } else 0L
    }

    val onDateClick = {
        val current = selectedDate ?: Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, y, m, d ->
                selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, d)
                }
                if (selectedTime == null) selectedTime = Calendar.getInstance()
            },
            current.get(Calendar.YEAR),
            current.get(Calendar.MONTH),
            current.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val onTimeClick = {
        val current = selectedTime ?: Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, h, min ->
                selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, h)
                    set(Calendar.MINUTE, min)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                if (selectedDate == null) selectedDate = Calendar.getInstance()
            },
            current.get(Calendar.HOUR_OF_DAY),
            current.get(Calendar.MINUTE),
            false
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (taskToEdit != null) "Edit Task" else "Add New Task",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Title *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            isError = showValidationError && title.isBlank(),
            supportingText = if (showValidationError && title.isBlank()) {
                { Text("Title is required") }
            } else null
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        CategoryDropdown(
            categories = categories,
            selectedCategory = category,
            onCategorySelected = { category = it },
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
            interactionSource = remember { MutableInteractionSource() }
                .also { source ->
                    LaunchedEffect(source) {
                        source.interactions.collect {
                            if (it is androidx.compose.foundation.interaction.PressInteraction.Press) {
                                onDateClick()
                            }
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
            interactionSource = remember { MutableInteractionSource() }
                .also { source ->
                    LaunchedEffect(source) {
                        source.interactions.collect {
                            if (it is androidx.compose.foundation.interaction.PressInteraction.Press) {
                                onTimeClick()
                            }
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
            onSelected = { priority = it },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isCompleted, onCheckedChange = { isCompleted = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mark as completed", style = MaterialTheme.typography.bodyMedium)
        }

        if (showValidationError) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Please fill in all required fields marked with *",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Button(
            onClick = {
                val hasError = title.isBlank() || category.isBlank() || selectedDate == null || selectedTime == null
                if (hasError) {
                    showValidationError = true
                } else {
                    showValidationError = false
                    val task = Task(
                        id = taskToEdit?.id ?: 0,
                        title = title.trim(),
                        description = description.trim(),
                        dueDate = dueDateMillis,
                        priority = priority,
                        category = category.trim(),
                        isCompleted = isCompleted
                    )
                    if (taskToEdit != null) {
                        viewModel.updateTask(task)
                    } else {
                        viewModel.addTask(task)
                    }
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (taskToEdit != null) "Update Task" else "Add Task")
        }

        if (dueDateMillis > 0) {
            Text(
                text = "Selected: ${Date(dueDateMillis)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
