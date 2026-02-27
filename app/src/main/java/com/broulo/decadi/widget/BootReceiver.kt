/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            if (ClockWidgetProvider.hasActiveWidgets(context) ||
                AnalogWidgetProvider.hasActiveWidgets(context) ||
                ProgressBarWidgetProvider.hasActiveWidgets(context)
            ) {
                WidgetUpdateService.start(context)
            }
        }
    }
}
