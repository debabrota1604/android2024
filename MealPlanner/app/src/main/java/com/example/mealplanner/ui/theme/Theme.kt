package com.example.mealplanner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography // ✅ Material 3 import
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1B5E20),
    secondary = Color(0xFFFFC107),
    tertiary = Color(0xFF2E7D32)
)

@Composable
fun MealPlannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
