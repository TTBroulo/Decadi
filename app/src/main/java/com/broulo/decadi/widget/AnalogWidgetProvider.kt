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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.broulo.decadi.MainActivity
import com.broulo.decadi.R
import com.broulo.decadi.data.SettingsRepository
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.clock.AnalogClockRenderer
import kotlin.math.min
import kotlin.math.roundToInt

class AnalogWidgetProvider : AppWidgetProvider() {

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
            !ProgressBarWidgetProvider.hasActiveWidgets(context) &&
            !FractionWidgetProvider.hasActiveWidgets(context)
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
                ComponentName(context, AnalogWidgetProvider::class.java)
            )

            val density = context.resources.displayMetrics.density
            val theme = settings.theme
            val openIntent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(
                context, 0, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            for (id in ids) {
                val options = manager.getAppWidgetOptions(id)
                val widthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 110)
                val heightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 110)
                val sizeDp = min(widthDp, heightDp)
                val sizePx = (sizeDp * density).roundToInt().coerceIn(100, 1000)

                val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)

                AnalogClockRenderer.draw(
                    canvas = canvas,
                    width = sizePx.toFloat(),
                    height = sizePx.toFloat(),
                    time = time,
                    bgColor = theme.background.toArgb(),
                    primaryColor = theme.primary.toArgb(),
                    secondaryColor = theme.secondary.toArgb(),
                    accentColor = theme.accent.toArgb(),
                    showSeconds = settings.showSeconds
                )

                val views = RemoteViews(context.packageName, R.layout.widget_analog)
                views.setImageViewBitmap(R.id.widget_analog_image, bitmap)
                views.setOnClickPendingIntent(R.id.widget_analog_root, pi)
                manager.updateAppWidget(id, views)
            }
        }

        fun hasActiveWidgets(context: Context): Boolean {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, AnalogWidgetProvider::class.java)
            )
            return ids.isNotEmpty()
        }
    }
}
