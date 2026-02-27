/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broulo.decadi.data.AppSettings
import com.broulo.decadi.data.ClockMode
import com.broulo.decadi.data.SettingsRepository
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.clock.AnalogClock
import com.broulo.decadi.ui.clock.DigitalClock
import com.broulo.decadi.ui.clock.ProgressBarClock
import com.broulo.decadi.ui.settings.SettingsScreen
import com.broulo.decadi.ui.theme.DecadiTheme
import com.broulo.decadi.ui.theme.LocalClockTheme
import com.broulo.decadi.widget.AnalogWidgetProvider
import com.broulo.decadi.widget.ClockWidgetProvider
import com.broulo.decadi.widget.ProgressBarWidgetProvider
import com.broulo.decadi.widget.WidgetUpdateService
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        enableEdgeToEdge()
        setContent {
            val repo = remember { SettingsRepository(this) }
            var settings by remember { mutableStateOf(repo.load()) }

            DecadiTheme(clockTheme = settings.theme) {
                var showSettings by remember { mutableStateOf(false) }

                if (showSettings) {
                    SettingsScreen(
                        settings = settings,
                        onSettingsChanged = { new ->
                            settings = new
                            repo.save(new)
                        },
                        onBack = { showSettings = false }
                    )
                } else {
                    ClockScreen(
                        settings = settings,
                        onOpenSettings = { showSettings = true }
                    )
                }
            }
        }
    }
}

@Composable
fun ClockScreen(
    settings: AppSettings,
    onOpenSettings: () -> Unit
) {
    val theme = LocalClockTheme.current
    val context = LocalContext.current
    var time by remember { mutableStateOf(DecimalTime.now()) }

    val delayMs = if (settings.showSeconds) 100L else 864L
    LaunchedEffect(settings.showSeconds) {
        while (true) {
            time = DecimalTime.now()
            delay(delayMs)
        }
    }

    // Restart widget service when settings change
    LaunchedEffect(settings.showSeconds, settings.theme) {
        if (ClockWidgetProvider.hasActiveWidgets(context) ||
            AnalogWidgetProvider.hasActiveWidgets(context) ||
            ProgressBarWidgetProvider.hasActiveWidgets(context)
        ) {
            WidgetUpdateService.start(context)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        when (settings.clockMode) {
            ClockMode.DIGITAL -> {
                DigitalClock(
                    time = time,
                    showSeconds = settings.showSeconds,
                    fontSize = settings.fontSizeSp.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            ClockMode.ANALOG -> {
                AnalogClock(
                    time = time,
                    showSeconds = settings.showSeconds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .align(Alignment.Center)
                )
            }
            ClockMode.PROGRESS_BAR -> {
                ProgressBarClock(
                    time = time,
                    showSeconds = settings.showSeconds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Text(
            text = "\u2699",
            color = theme.secondary.copy(alpha = 0.5f),
            fontSize = 28.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 20.dp)
                .clickable { onOpenSettings() }
        )
    }
}
