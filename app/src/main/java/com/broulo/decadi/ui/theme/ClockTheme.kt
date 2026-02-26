/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
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
