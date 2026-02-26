package com.broulo.decadi.widget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
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
    private val scope = CoroutineScope(Dispatchers.Default)

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
        startUpdating()
        return START_STICKY
    }

    private fun startUpdating() {
        job?.cancel()
        job = scope.launch {
            while (isActive) {
                ClockWidgetProvider.updateWidgets(applicationContext)
                delay(864L) // 1 decimal second = 0.864 standard seconds
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
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
