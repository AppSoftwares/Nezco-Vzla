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
    primaryContainer = NezcoOrangeActive.copy(alpha = 0.12f),
    onPrimaryContainer = NezcoOrangeActive,
    secondary = NezcoBarDark,
    onSecondary = Color.White,
    secondaryContainer = NezcoLightBorder,
    onSecondaryContainer = NezcoLightTextPrimary,
    tertiary = NezcoBrass,
    onTertiary = Color.White,
    background = NezcoLightBg,
    onBackground = NezcoLightTextPrimary,
    surface = NezcoLightSurface,
    onSurface = NezcoLightTextPrimary,
    surfaceVariant = NezcoLightBg,
    onSurfaceVariant = NezcoLightTextSecondary,
    outline = NezcoLightBorder,
    outlineVariant = NezcoLightBorder.copy(alpha = 0.5f)
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
