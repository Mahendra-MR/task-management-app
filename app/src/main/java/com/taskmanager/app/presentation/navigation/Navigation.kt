package com.taskmanager.app.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.taskmanager.app.domain.model.Task
import com.taskmanager.app.presentation.screen.*
import com.taskmanager.app.presentation.viewmodel.TaskViewModel
import com.google.gson.Gson

object Routes {
    const val HOME = "home"
    const val TASK_LIST = "task_list"
    const val TASK_LIST_WITH_CATEGORY = "task_list_with_category"
    const val ADD_TASK = "add_task"
    const val EDIT_TASK = "edit_task"
    const val TASK_DETAILS = "task_details"
    const val CATEGORIES = "categories"
}

@Composable
fun AppNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()
    val gson = remember { Gson() }

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }

        // 🔹 Unfiltered Task List
        composable(Routes.TASK_LIST) {
            TaskListScreen(
                viewModel = viewModel,
                categoryFilter = "",
                onTaskClick = { task ->
                    val json = Uri.encode(gson.toJson(task))
                    navController.navigate("${Routes.TASK_DETAILS}/$json")
                },
                onEditTask = { task ->
                    val json = Uri.encode(gson.toJson(task))
                    navController.navigate("${Routes.EDIT_TASK}/$json")
                }
            )
        }

        // 🔹 Filtered Task List
        composable(
            route = "${Routes.TASK_LIST_WITH_CATEGORY}/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            TaskListScreen(
                viewModel = viewModel,
                categoryFilter = category,
                onTaskClick = { task ->
                    val json = Uri.encode(gson.toJson(task))
                    navController.navigate("${Routes.TASK_DETAILS}/$json")
                },
                onEditTask = { task ->
                    val json = Uri.encode(gson.toJson(task))
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
            "${Routes.EDIT_TASK}/{task}",
            arguments = listOf(navArgument("task") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("task")
            val task = gson.fromJson(taskJson, Task::class.java)
            AddEditTaskScreen(
                viewModel = viewModel,
                taskToEdit = task,
                onSave = { navController.popBackStack() }
            )
        }

        composable(
            "${Routes.TASK_DETAILS}/{task}",
            arguments = listOf(navArgument("task") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskJson = backStackEntry.arguments?.getString("task")
            val task = gson.fromJson(taskJson, Task::class.java)
            TaskDetailsScreen(
                task = task,
                viewModel = viewModel,
                onEdit = {
                    val json = Uri.encode(gson.toJson(task))
                    navController.navigate("${Routes.EDIT_TASK}/$json")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // 🔹 Categories Screen
        composable(Routes.CATEGORIES) {
            CategoryManagementScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onViewTasksForCategory = { category ->
                    navController.navigate("${Routes.TASK_LIST_WITH_CATEGORY}/$category")
                }
            )
        }
    }
}
