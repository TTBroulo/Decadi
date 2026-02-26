/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.broulo.decadi.MainActivity
import com.broulo.decadi.R
import com.broulo.decadi.data.SettingsRepository
import com.broulo.decadi.model.DecimalTime

class ClockWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateWidgets(context)
        WidgetUpdateService.start(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        updateWidgets(context)
    }

    override fun onDisabled(context: Context) {
        if (!AnalogWidgetProvider.hasActiveWidgets(context)) {
            WidgetUpdateService.stop(context)
        }
    }

    companion object {
        fun updateWidgets(context: Context) {
            val settings = SettingsRepository(context).load()
            val time = DecimalTime.now()
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, ClockWidgetProvider::class.java)
            )

            val theme = settings.theme
            val openIntent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(
                context, 0, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            for (id in ids) {
                val options = manager.getAppWidgetOptions(id)
                val heightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 60)
                val widthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 180)

                // "H:MM" = 4 chars worth of width; with seconds ":SS" adds ~2 more narrow chars
                val charSlots = if (settings.showSeconds) 4.5f else 3.2f
                val sizeFromHeight = heightDp * 0.9f
                val sizeFromWidth = widthDp / charSlots
                val mainSizeSp = minOf(sizeFromHeight, sizeFromWidth).coerceIn(14f, 200f)
                val secSizeSp = (mainSizeSp * 0.55f).coerceIn(10f, 110f)

                val views = RemoteViews(context.packageName, R.layout.widget_clock)

                views.setTextViewText(R.id.widget_hours, "${time.hour}")
                views.setTextViewText(R.id.widget_minutes, time.minute.toString().padStart(2, '0'))

                views.setTextViewTextSize(R.id.widget_hours, TypedValue.COMPLEX_UNIT_SP, mainSizeSp)
                views.setTextViewTextSize(R.id.widget_separator1, TypedValue.COMPLEX_UNIT_SP, mainSizeSp)
                views.setTextViewTextSize(R.id.widget_minutes, TypedValue.COMPLEX_UNIT_SP, mainSizeSp)

                views.setInt(R.id.widget_root, "setBackgroundColor", theme.background.toArgb())
                views.setTextColor(R.id.widget_hours, theme.primary.toArgb())
                views.setTextColor(R.id.widget_separator1, theme.secondary.toArgb())
                views.setTextColor(R.id.widget_minutes, theme.primary.toArgb())

                if (settings.showSeconds) {
                    views.setViewVisibility(R.id.widget_separator2, View.VISIBLE)
                    views.setViewVisibility(R.id.widget_seconds, View.VISIBLE)
                    views.setTextViewText(R.id.widget_seconds, time.second.toString().padStart(2, '0'))
                    views.setTextViewTextSize(R.id.widget_separator2, TypedValue.COMPLEX_UNIT_SP, secSizeSp)
                    views.setTextViewTextSize(R.id.widget_seconds, TypedValue.COMPLEX_UNIT_SP, secSizeSp)
                    views.setTextColor(R.id.widget_separator2, theme.secondary.toArgb())
                    views.setTextColor(R.id.widget_seconds, theme.secondary.toArgb())
                } else {
                    views.setViewVisibility(R.id.widget_separator2, View.GONE)
                    views.setViewVisibility(R.id.widget_seconds, View.GONE)
                }

                views.setOnClickPendingIntent(R.id.widget_root, pi)
                manager.updateAppWidget(id, views)
            }
        }

        fun hasActiveWidgets(context: Context): Boolean {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, ClockWidgetProvider::class.java)
            )
            return ids.isNotEmpty()
        }
    }
}
