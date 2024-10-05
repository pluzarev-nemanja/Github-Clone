package com.example.github.core.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ShadesOfGreenGenerator {
    @Composable
    fun GetShadeOfGreen(commitCount: Int): Color {
        if (commitCount == 0) return Color(MaterialTheme.colorScheme.onPrimaryContainer.value)
        val alpha = (commitCount / 10.0).toFloat().coerceIn(0f, 1f)
        return Color(0f, 1f, 0f, alpha)
    }
}
