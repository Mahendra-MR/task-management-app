package com.taskmanager.app.presentation.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.taskmanager.app.presentation.navigation.Routes

@Composable
fun NavigationButtons(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationButton("My Tasks") { navController.navigate(Routes.TASK_LIST) }
        NavigationButton("New Task") { navController.navigate(Routes.ADD_TASK) }
        NavigationButton("Categories") { navController.navigate(Routes.CATEGORIES) }
    }
}

@Composable
fun NavigationButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .width(300.dp)
            .height(48.dp)
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}
