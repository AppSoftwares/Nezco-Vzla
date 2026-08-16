package com.nezco.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Editorial Aesthetic Dark Color Scheme
private val EditorialColorScheme = darkColorScheme(
    primary = EditorialRed,
    onPrimary = Color.White,
    primaryContainer = EditorialRedContainer,
    onPrimaryContainer = EditorialRedLight,
    secondary = EditorialRed,
    onSecondary = Color.White,
    secondaryContainer = EditorialSurfaceElevated,
    onSecondaryContainer = EditorialTextPrimary,
    tertiary = EditorialAmber,
    onTertiary = Color.Black,
    tertiaryContainer = EditorialAmberContainer,
    onTertiaryContainer = EditorialAmber,
    background = EditorialBg,
    onBackground = EditorialTextBody,
    surface = EditorialSurface,
    onSurface = EditorialTextPrimary,
    surfaceVariant = EditorialSurfaceSub,
    onSurfaceVariant = EditorialTextMuted,
    outline = EditorialBorder,
    outlineVariant = EditorialBorderLight
)

@Composable
fun NezcoTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EditorialColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    NezcoTheme(content = content)
}
