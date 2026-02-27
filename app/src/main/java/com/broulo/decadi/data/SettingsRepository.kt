/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.broulo.decadi.ui.theme.ClockTheme

enum class ClockMode { DIGITAL, ANALOG, PROGRESS_BAR, FRACTION }

data class AppSettings(
    val showSeconds: Boolean = false,
    val fontSizeSp: Int = 96,
    val clockMode: ClockMode = ClockMode.DIGITAL,
    val theme: ClockTheme = ClockTheme(
        background = Color(0xFF000000),
        primary = Color(0xFFFFFFFF),
        secondary = Color(0xFF888888),
        accent = Color(0xFFFF4444)
    )
)

class SettingsRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): AppSettings {
        return AppSettings(
            showSeconds = prefs.getBoolean(KEY_SHOW_SECONDS, false),
            fontSizeSp = prefs.getInt(KEY_FONT_SIZE, 96),
            clockMode = try {
                ClockMode.valueOf(prefs.getString(KEY_CLOCK_MODE, ClockMode.DIGITAL.name)!!)
            } catch (_: Exception) {
                ClockMode.DIGITAL
            },
            theme = ClockTheme(
                background = Color(prefs.getInt(KEY_COLOR_BG, 0xFF000000.toInt())),
                primary = Color(prefs.getInt(KEY_COLOR_PRIMARY, 0xFFFFFFFF.toInt())),
                secondary = Color(prefs.getInt(KEY_COLOR_SECONDARY, 0xFF888888.toInt())),
                accent = Color(prefs.getInt(KEY_COLOR_ACCENT, 0xFFFF4444.toInt()))
            )
        )
    }

    fun save(settings: AppSettings) {
        prefs.edit()
            .putBoolean(KEY_SHOW_SECONDS, settings.showSeconds)
            .putInt(KEY_FONT_SIZE, settings.fontSizeSp)
            .putString(KEY_CLOCK_MODE, settings.clockMode.name)
            .putInt(KEY_COLOR_BG, settings.theme.background.toArgb())
            .putInt(KEY_COLOR_PRIMARY, settings.theme.primary.toArgb())
            .putInt(KEY_COLOR_SECONDARY, settings.theme.secondary.toArgb())
            .putInt(KEY_COLOR_ACCENT, settings.theme.accent.toArgb())
            .apply()
    }

    companion object {
        const val PREFS_NAME = "decadi_settings"
        const val KEY_SHOW_SECONDS = "show_seconds"
        const val KEY_FONT_SIZE = "font_size"
        const val KEY_CLOCK_MODE = "clock_mode"
        const val KEY_COLOR_BG = "color_bg"
        const val KEY_COLOR_PRIMARY = "color_primary"
        const val KEY_COLOR_SECONDARY = "color_secondary"
        const val KEY_COLOR_ACCENT = "color_accent"
    }
}
