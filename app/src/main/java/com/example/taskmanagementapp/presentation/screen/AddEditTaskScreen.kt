package com.example.taskmanagementapp.presentation.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapp.domain.model.Priority
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.presentation.components.PriorityDropdown
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    taskToEdit: Task? = null,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(taskToEdit?.description ?: "") }
    var category by remember { mutableStateOf(taskToEdit?.category ?: "") }
    var priority by remember { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIUM) }
    var isCompleted by remember { mutableStateOf(taskToEdit?.isCompleted ?: false) }

    var selectedDateText by remember {
        mutableStateOf(
            if (taskToEdit != null) dateFormat.format(Date(taskToEdit.dueDate)) else ""
        )
    }
    var selectedTimeText by remember {
        mutableStateOf(
            if (taskToEdit != null) timeFormat.format(Date(taskToEdit.dueDate)) else ""
        )
    }

    var dueDateMillis by remember {
        mutableLongStateOf(taskToEdit?.dueDate ?: 0L)
    }

    var showValidationError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📅 Date Picker
        TextField(
            value = selectedDateText,
            onValueChange = {},
            label = { Text("Select Due Date") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val now = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            calendar.set(Calendar.YEAR, year)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.DAY_OF_MONTH, day)

                            selectedDateText = dateFormat.format(calendar.time)

                            // If time already picked, update full dueDateMillis
                            if (selectedTimeText.isNotBlank()) {
                                dueDateMillis = calendar.timeInMillis
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ⏰ Time Picker
        TextField(
            value = selectedTimeText,
            onValueChange = {},
            label = { Text("Select Due Time") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val now = Calendar.getInstance()
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)

                            selectedTimeText = timeFormat.format(calendar.time)

                            // If date already picked, update full dueDateMillis
                            if (selectedDateText.isNotBlank()) {
                                dueDateMillis = calendar.timeInMillis
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                    ).show()
                },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        PriorityDropdown(selected = priority, onSelected = { priority = it })

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isCompleted, onCheckedChange = { isCompleted = it })
            Text("Completed")
        }

        if (showValidationError) {
            Text(
                "Title, Category, and Due Date/Time are required",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (title.isBlank() || category.isBlank() || dueDateMillis == 0L) {
                    showValidationError = true
                } else {
                    val task = Task(
                        id = taskToEdit?.id ?: 0,
                        title = title,
                        description = description,
                        dueDate = dueDateMillis,
                        priority = priority,
                        category = category,
                        isCompleted = isCompleted
                    )
                    if (taskToEdit != null) viewModel.updateTask(task)
                    else viewModel.addTask(task)
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (taskToEdit != null) "Update Task" else "Add Task")
        }
    }
}
