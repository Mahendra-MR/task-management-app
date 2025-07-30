package com.example.taskmanagementapp.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmanagementapp.domain.model.Task
import com.example.taskmanagementapp.presentation.screen.*
import com.example.taskmanagementapp.presentation.viewmodel.TaskViewModel
import com.google.gson.Gson

object Routes {
    const val HOME = "home"
    const val TASK_LIST = "task_list"
    const val ADD_TASK = "add_task"
    const val EDIT_TASK = "edit_task"
    const val TASK_DETAILS = "task_details"
    const val CATEGORIES = "categories"
}

@Composable
fun AppNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }

        composable(Routes.TASK_LIST) {
            TaskListScreen(
                viewModel = viewModel,
                onTaskClick = { task ->
                    val json = Uri.encode(Gson().toJson(task))
                    navController.navigate("${Routes.TASK_DETAILS}/$json")
                },
                onEditTask = { task ->
                    val json = Uri.encode(Gson().toJson(task))
                    navController.navigate("${Routes.EDIT_TASK}/$json")
                }
            )
        }

        composable(Routes.ADD_TASK) {
            AddEditTaskScreen(
                viewModel = viewModel,
                onSave = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.EDIT_TASK}/{task}",
            arguments = listOf(navArgument("task") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("task")
            val task = Gson().fromJson(taskJson, Task::class.java)
            AddEditTaskScreen(
                viewModel = viewModel,
                taskToEdit = task,
                onSave = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.TASK_DETAILS}/{task}",
            arguments = listOf(navArgument("task") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("task")
            val task = Gson().fromJson(taskJson, Task::class.java)
            TaskDetailsScreen(
                task = task,
                viewModel = viewModel,
                onEdit = {
                    val json = Uri.encode(Gson().toJson(task))
                    navController.navigate("${Routes.EDIT_TASK}/$json")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CATEGORIES) {
            CategoryManagementScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}