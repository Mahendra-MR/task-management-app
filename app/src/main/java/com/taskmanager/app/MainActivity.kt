package com.taskmanager.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.taskmanager.app.di.AppModule
import com.taskmanager.app.presentation.navigation.AppNavigation
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.taskmanager.app.ui.theme.TaskManagementAppTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DI
        AppModule.init(applicationContext)

        enableEdgeToEdge()

        setContent {
            TaskManagementAppTheme {
                val viewModel: TaskViewModel = viewModel(factory = AppModule.viewModelFactory)

                // Load initial data once on app startup
                LaunchedEffect(Unit) {
                    viewModel.loadTasks()
                    viewModel.loadCategories()
                    viewModel.loadQuote()
                }

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
        }
    }
}
