package com.example.studybuddy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BrandPurple,
    secondary = BrandPurpleDark,
    background = SurfaceLight,
    surface = Color.White,
    surfaceVariant = Color(0xFFEFEFF5)
)

private val DarkColors = darkColorScheme(
    primary = BrandPurpleLight,
    secondary = BrandPurple,
    background = SurfaceDark,
    surface = CardDark,
    surfaceVariant = Color(0xFF2E2839)
)

/**
 * darkTheme now comes from the user's Settings toggle (passed in from MainActivity),
 * not just the system default -- see MainActivity.kt.
 */
@Composable
fun StudyBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = StudyBuddyTypography,
        content = content
    )
}
