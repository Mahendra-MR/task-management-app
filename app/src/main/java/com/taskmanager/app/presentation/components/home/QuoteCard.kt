package com.taskmanager.app.presentation.components.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskmanager.app.domain.model.Quote

@Composable
fun QuoteCard(
    quote: Quote?,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit
) {
    val isPreview = LocalInspectionMode.current
    val isDarkTheme = isSystemInDarkTheme()

    // 🌗 Theme-based colors
    val backgroundColor = if (isDarkTheme) Color(0xFF3A3A4F) else Color(0xFFFFE6F0)
    val textColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFF1A1A1A)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Fetching quote...",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                }

                error != null -> {
                    Text(
                        text = "Failed to load quote.\n$error",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }

                quote != null -> {
                    Text(
                        text = "\"${quote.content}\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "— ${quote.author}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = textColor.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center
                    )
                }

                else -> {
                    Text(
                        text = "Welcome to your task dashboard!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                }
            }
        }
    }
}
