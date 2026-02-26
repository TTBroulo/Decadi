package com.broulo.decadi.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class ClockTheme(
    val background: Color,
    val primary: Color,
    val secondary: Color,
    val accent: Color
)

val DefaultClockTheme = ClockTheme(
    background = Color(0xFF000000),
    primary = Color(0xFFFFFFFF),
    secondary = Color(0xFF888888),
    accent = Color(0xFFFF4444)
)

val LocalClockTheme = compositionLocalOf { DefaultClockTheme }
