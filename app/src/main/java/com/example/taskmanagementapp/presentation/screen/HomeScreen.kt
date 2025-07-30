package com.example.taskmanagementapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskmanagementapp.presentation.navigation.Routes
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel

@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Task Management",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate(Routes.TASK_LIST) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Tasks")
            }
            Button(
                onClick = { navController.navigate(Routes.ADD_TASK) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add New Task")
            }
            Button(
                onClick = { navController.navigate(Routes.CATEGORIES) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Categories")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Quote section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Loading motivational quote...",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    state.error != null -> {
                        Text(
                            text = state.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.retryLoadQuote() }) {
                            Text("Retry")
                        }
                    }

                    state.quote != null -> {
                        Text(
                            text = "\"${state.quote!!.content}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "— ${state.quote!!.author}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        Text(
                            text = "Welcome to Task Management!",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}