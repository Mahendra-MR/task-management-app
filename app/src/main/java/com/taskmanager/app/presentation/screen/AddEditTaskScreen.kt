package com.taskmanager.app.presentation.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.taskmanager.app.domain.model.*
import com.taskmanager.app.presentation.components.edit.*
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import androidx.compose.runtime.saveable.rememberSaveable
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
    val state by viewModel.state.collectAsState()

    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    var title by rememberSaveable { mutableStateOf(taskToEdit?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(taskToEdit?.description ?: "") }
    var category by rememberSaveable { mutableStateOf(taskToEdit?.category ?: "") }
    var priority by rememberSaveable { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIUM) }
    var isCompleted by rememberSaveable { mutableStateOf(taskToEdit?.isCompleted ?: false) }
    var showValidationError by rememberSaveable { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    var selectedTime by remember { mutableStateOf<Calendar?>(null) }

    LaunchedEffect(taskToEdit) {
        taskToEdit?.dueDate?.takeIf { it > 0 }?.let {
            val cal = Calendar.getInstance().apply { timeInMillis = it }
            selectedDate = cal.clone() as Calendar
            selectedTime = cal.clone() as Calendar
        }
    }

    // ✅ This is now valid again since we made it public
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    val dueDateMillis by remember(selectedDate, selectedTime) {
        derivedStateOf {
            if (selectedDate != null && selectedTime != null) {
                (selectedDate!!.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, selectedTime!!.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, selectedTime!!.get(Calendar.MINUTE))
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            } else 0L
        }
    }

    val selectedDateText = selectedDate?.let { dateFormat.format(it.time) } ?: ""
    val selectedTimeText = selectedTime?.let { timeFormat.format(it.time) } ?: ""

    val onDateClick = {
        val current = selectedDate ?: Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, y, m, d ->
                selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, d)
                }.also {
                    if (selectedTime == null) selectedTime = Calendar.getInstance()
                }
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
            { _, h, m ->
                selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, h)
                    set(Calendar.MINUTE, m)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.also {
                    if (selectedDate == null) selectedDate = Calendar.getInstance()
                }
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

        TaskFormFields(
            title = title,
            onTitleChange = { title = it },
            description = description,
            onDescriptionChange = { description = it },
            category = category,
            onCategoryChange = { category = it },
            onNavigateToCategories = onNavigateToCategories,
            priority = priority,
            onPriorityChange = { priority = it },
            selectedDateText = selectedDateText,
            selectedTimeText = selectedTimeText,
            onDateClick = onDateClick,
            onTimeClick = onTimeClick,
            selectedDate = selectedDate,
            selectedTime = selectedTime,
            showValidationError = showValidationError,
            categories = state.categories
        )

        CompletionCheckbox(isCompleted = isCompleted) { isCompleted = it }

        if (showValidationError) ValidationMessage()

        SaveButton(
            isEdit = taskToEdit != null,
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
                    if (taskToEdit != null) viewModel.updateTask(task)
                    else viewModel.addTask(task)
                    onSave()
                }
            }
        )

        if (dueDateMillis > 0) {
            Text(
                text = "Selected: ${Date(dueDateMillis)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
