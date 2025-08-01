package com.taskmanager.app.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.taskmanager.app.presentation.components.home.*
import androidx.compose.ui.Alignment

@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val highPriorityTasks by viewModel.highPriorityTasks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Task Management App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        NavigationButtons(navController)

        PriorityTasksCard(tasks = highPriorityTasks, navController = navController)

        QuoteCard(
            quote = state.quote,
            isLoading = state.isLoading,
            error = state.error,
            onRetry = { viewModel.retryLoadQuote() }
        )
    }
}
