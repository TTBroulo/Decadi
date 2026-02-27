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
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.broulo.decadi.MainActivity
import com.broulo.decadi.R
import com.broulo.decadi.data.SettingsRepository
import com.broulo.decadi.model.DecimalTime

class FractionWidgetProvider : AppWidgetProvider() {

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
        if (!ClockWidgetProvider.hasActiveWidgets(context) &&
            !AnalogWidgetProvider.hasActiveWidgets(context) &&
            !ProgressBarWidgetProvider.hasActiveWidgets(context)
        ) {
            WidgetUpdateService.stop(context)
        }
    }

    companion object {
        fun updateWidgets(context: Context) {
            val settings = SettingsRepository(context).load()
            val time = DecimalTime.now()
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, FractionWidgetProvider::class.java)
            )

            val theme = settings.theme
            val openIntent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(
                context, 0, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val totalDecimalSeconds = time.hour * 10000 + time.minute * 100 + time.second
            val digits = if (settings.showSeconds) {
                String.format("%05d", totalDecimalSeconds)
            } else {
                String.format("%03d", totalDecimalSeconds / 100)
            }

            for (id in ids) {
                val options = manager.getAppWidgetOptions(id)
                val heightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 60)
                val widthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 110)

                // charSlots: "0." prefix counts as ~1.5, digits are 5 or 3
                val charSlots = if (settings.showSeconds) 6.5f else 4.5f
                val sizeFromHeight = heightDp * 0.9f
                val sizeFromWidth = widthDp / charSlots
                val mainSizeSp = minOf(sizeFromHeight, sizeFromWidth).coerceIn(14f, 200f)
                val prefixSizeSp = (mainSizeSp * 0.55f).coerceIn(10f, 110f)

                val views = RemoteViews(context.packageName, R.layout.widget_fraction)

                views.setTextViewText(R.id.widget_fraction_prefix, "0.")
                views.setTextViewText(R.id.widget_fraction_digits, digits)

                views.setTextViewTextSize(R.id.widget_fraction_prefix, TypedValue.COMPLEX_UNIT_SP, prefixSizeSp)
                views.setTextViewTextSize(R.id.widget_fraction_digits, TypedValue.COMPLEX_UNIT_SP, mainSizeSp)

                views.setInt(R.id.widget_fraction_root, "setBackgroundColor", theme.background.toArgb())
                views.setTextColor(R.id.widget_fraction_prefix, theme.secondary.toArgb())
                views.setTextColor(R.id.widget_fraction_digits, theme.primary.toArgb())

                views.setOnClickPendingIntent(R.id.widget_fraction_root, pi)
                manager.updateAppWidget(id, views)
            }
        }

        fun hasActiveWidgets(context: Context): Boolean {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, FractionWidgetProvider::class.java)
            )
            return ids.isNotEmpty()
        }
    }
}
