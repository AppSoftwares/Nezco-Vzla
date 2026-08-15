package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Editorial Aesthetic Palette
val EditorialBg = Color(0xFF0F1115)             // Deep Obsidian Canvas
val EditorialSurface = Color(0xFF1A1C1E)        // Elevated Dark Card Surface
val EditorialSurfaceSub = Color(0xFF25272B)     // Nested Component Surface
val EditorialSurfaceElevated = Color(0xFF2C2E33)// Interactive Button / Chip Surface
val EditorialBorder = Color(0xFF2C2E33)         // Fine Structural Border
val EditorialBorderLight = Color(0xFF3F4146)    // Secondary Border

val EditorialRed = Color(0xFFD13438)            // Signature Crimson Accent
val EditorialRedPressed = Color(0xFFA4262C)     // Deep Pressed Crimson
val EditorialRedContainer = Color(0x33D13438)   // 20% Alpha Crimson Container
val EditorialRedLight = Color(0xFFE84D51)

val EditorialTextPrimary = Color(0xFFFFFFFF)    // Crisp White Heading
val EditorialTextBody = Color(0xFFE2E2E6)       // Clean Off-White Body
val EditorialTextMuted = Color(0xFF8E9196)      // Muted Uppercase Micro-Label

val EditorialAmber = Color(0xFFF59E0B)          // Warning Amber
val EditorialAmberContainer = Color(0x33F59E0B)
val EditorialGreen = Color(0xFF10B981)          // Success Green
val EditorialGreenContainer = Color(0x3310B981)
val EditorialBlue = Color(0xFF38BDF8)           // Cyan Accent
val EditorialBlueContainer = Color(0x3338BDF8)

// Compatibility Aliases for existing Nezco references
val NezcoNavy = EditorialSurface
val NezcoNavyDark = EditorialBg
val NezcoNavyLight = EditorialSurfaceElevated
val NezcoBlueAccent = EditorialBlue

val NezcoSafetyRed = EditorialRed
val NezcoSafetyRedLight = EditorialRedLight
val NezcoSafetyRedContainer = EditorialRedContainer

val NezcoAmber = EditorialAmber
val NezcoAmberGold = Color(0xFFFBBF24)
val NezcoAmberContainer = EditorialAmberContainer

val NezcoGreen = EditorialGreen
val NezcoGreenContainer = EditorialGreenContainer

val NezcoSlateBg = EditorialBg
val NezcoCardBg = EditorialSurface
val NezcoSurfaceVariant = EditorialSurfaceSub
val NezcoBorder = EditorialBorder
val NezcoTextPrimary = EditorialTextPrimary
val NezcoTextSecondary = EditorialTextMuted
val NezcoTextMuted = EditorialTextMuted
