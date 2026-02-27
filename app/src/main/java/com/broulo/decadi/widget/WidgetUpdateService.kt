/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.widget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.broulo.decadi.MainActivity
import com.broulo.decadi.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WidgetUpdateService : Service() {

    private var job: Job? = null
    private var receiverRegistered = false
    private val scope = CoroutineScope(Dispatchers.Default)

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_ON -> {
                    Log.d(TAG, "Screen ON — resuming widget updates")
                    startUpdating()
                }
                Intent.ACTION_SCREEN_OFF -> {
                    Log.d(TAG, "Screen OFF — pausing widget updates")
                    stopUpdating()
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            buildNotification(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            else 0
        )

        if (!receiverRegistered) {
            registerReceiver(screenReceiver, IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
            })
            receiverRegistered = true
        }

        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (pm.isInteractive) {
            startUpdating()
        }

        return START_STICKY
    }

    private fun startUpdating() {
        job?.cancel()
        job = scope.launch {
            while (isActive) {
                ClockWidgetProvider.updateWidgets(applicationContext)
                AnalogWidgetProvider.updateWidgets(applicationContext)
                ProgressBarWidgetProvider.updateWidgets(applicationContext)
                delay(864L) // 1 decimal second = 0.864 standard seconds
            }
        }
    }

    private fun stopUpdating() {
        job?.cancel()
        job = null
    }

    override fun onDestroy() {
        job?.cancel()
        try { unregisterReceiver(screenReceiver) } catch (_: Exception) {}
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Widget horloge",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(false)
            description = "Maintient le widget horloge \u00e0 jour"
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val openIntent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            this, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("D\u00e9cadi")
            .setContentText("Widget actif")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(pi)
            .setSilent(true)
            .build()
    }

    companion object {
        private const val TAG = "WidgetUpdateService"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "widget_updates"

        fun start(context: Context) {
            val intent = Intent(context, WidgetUpdateService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, WidgetUpdateService::class.java))
        }
    }
}
