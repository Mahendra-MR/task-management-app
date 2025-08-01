package com.taskmanager.app.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.taskmanager.app.domain.model.Priority
import com.taskmanager.app.presentation.navigation.Routes
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.taskmanager.app.presentation.components.*

@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    navController: NavController
) {
    // Optimized state collection
    val tasks by viewModel.tasks.collectAsState()
    val quote by viewModel.quote.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val highPriorityTasks by viewModel.highPriorityTasks.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // App Title
        Text(
            text = "Task Management App",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Navigation Buttons
        NavigationButtons(
            onTasksClick = { navController.navigate(Routes.TASK_LIST) },
            onNewTaskClick = { navController.navigate(Routes.ADD_TASK) },
            onCategoriesClick = { navController.navigate(Routes.CATEGORIES) }
        )

        // High Priority Tasks Section
        HighPriorityTasksSection(
            tasks = highPriorityTasks,
            onViewAllClick = { navController.navigate(Routes.TASK_LIST) }
        )

        // Motivational Quote Section
        QuoteCard(
            quote = quote,
            isLoading = isLoading,
            error = error,
            onRetry = { viewModel.retryLoadQuote() }
        )
    }
}
