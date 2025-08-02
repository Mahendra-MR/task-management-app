package com.taskmanager.app.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.taskmanager.app.presentation.components.home.*

@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    navController: NavController
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    val previewTasks = remember(homeUiState.highPriorityPreviewTasks) {
        homeUiState.highPriorityPreviewTasks
    }

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

        PriorityTasksCard(
            tasks = previewTasks,
            navController = navController
        )

        QuoteCard(
            quote = homeUiState.quote,
            isLoading = homeUiState.isLoading,
            error = homeUiState.error,
            onRetry = rememberUpdatedState(newValue = { viewModel.retryLoadQuote() }).value
        )
    }
}
