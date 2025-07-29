package com.example.taskmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagementapp.di.AppModule
import com.example.taskmanagementapp.presentation.navigation.AppNavigation
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel
import com.example.taskmanagementapp.ui.theme.TaskManagementAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DI module
        AppModule.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            TaskManagementAppTheme {
                val viewModel: TaskViewModel = viewModel(factory = AppModule.viewModelFactory)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Start full app navigation
                        AppNavigation(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
