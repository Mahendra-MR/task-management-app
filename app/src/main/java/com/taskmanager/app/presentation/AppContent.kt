package com.taskmanager.app.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taskmanager.app.presentation.navigation.AppNavigation
import com.taskmanager.app.presentation.viewmodel.TaskViewModel

@Composable
fun AppContent(viewModel: TaskViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation(viewModel = viewModel)
        }
    }
}
