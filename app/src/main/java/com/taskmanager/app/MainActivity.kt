package com.taskmanager.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.taskmanager.app.di.AppModule
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.taskmanager.app.ui.theme.TaskManagementAppTheme
import com.taskmanager.app.presentation.AppContent


class MainActivity : ComponentActivity() {

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppModule.init(applicationContext)
        taskViewModel = ViewModelProvider(this, AppModule.viewModelFactory)[TaskViewModel::class.java]

        enableEdgeToEdge()

        // Preload data before UI renders
        taskViewModel.refreshData()
        taskViewModel.loadQuote()

        setContent {
            TaskManagementAppTheme {
                AppContent(viewModel = taskViewModel)
            }
        }
    }
}
