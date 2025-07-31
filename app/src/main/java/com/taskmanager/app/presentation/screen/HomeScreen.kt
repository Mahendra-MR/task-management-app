package com.taskmanager.app.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.taskmanager.app.domain.model.Priority
import com.taskmanager.app.presentation.navigation.Routes
import com.taskmanager.app.presentation.viewmodel.TaskViewModel

@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title
        Text(
            text = "Task Management App",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Navigation Buttons (arranged vertically if needed)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationButton("My Tasks") {
                navController.navigate(Routes.TASK_LIST)
            }
            NavigationButton("New Task") {
                navController.navigate(Routes.ADD_TASK)
            }
            NavigationButton("Categories") {
                navController.navigate(Routes.CATEGORIES)
            }
        }

        // Priority Tasks
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val pendingPriorityTasks = state.tasks.filter {
                    it.priority == Priority.HIGH && !it.isCompleted
                }

                if (pendingPriorityTasks.isNotEmpty()) {
                    Text(
                        text = "High Priority Tasks",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    pendingPriorityTasks.take(3).forEach { task ->
                        Text("• ${task.title}", style = MaterialTheme.typography.bodyMedium)
                    }
                    TextButton(
                        onClick = { navController.navigate(Routes.TASK_LIST) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("View All")
                    }
                } else {
                    Text(
                        text = "No high-priority pending tasks.",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Create a new task or explore your existing ones.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Motivational Quote
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEE4FF)) // Elegant purple
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Fetching quote...")
                    }

                    state.error != null -> {
                        Text(
                            text = state.error ?: "Error loading quote.",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.retryLoadQuote() }) {
                            Text("Retry")
                        }
                    }

                    state.quote != null -> {
                        Text(
                            text = "\"${state.quote!!.content}\"",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "— ${state.quote!!.author}",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        Text("Welcome to your task dashboard!")
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(300.dp)
            .height(48.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}
