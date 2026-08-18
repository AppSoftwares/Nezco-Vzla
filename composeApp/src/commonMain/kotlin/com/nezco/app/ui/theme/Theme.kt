package com.nezco.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Editorial Aesthetic Dark Color Scheme
private val EditorialDarkColorScheme = darkColorScheme(
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

// Sophisticated Brand Light Color Scheme
private val NezcoLightColorScheme = lightColorScheme(
    primary = NezcoOrangeActive,
    onPrimary = Color.White,
    primaryContainer = NezcoOrangeActive.copy(alpha = 0.15f),
    onPrimaryContainer = Color(0xFF9F4500), // Darker orange for text
    secondary = NezcoBarDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2E2E6),
    onSecondaryContainer = Color(0xFF1A1C1E),
    tertiary = NezcoBrass,
    onTertiary = Color.White,
    background = Color(0xFFF0F2F5), // Slightly darker background for depth
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE9ECEF),
    onSurfaceVariant = Color(0xFF495057), // Darker gray for better readability
    outline = Color(0xFFCED4DA), // Darker border for better distinction
    outlineVariant = Color(0xFFDEE2E6)
)

@Composable
fun NezcoTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) EditorialDarkColorScheme else NezcoLightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
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
    NezcoTheme(darkTheme = darkTheme, content = content)
}
