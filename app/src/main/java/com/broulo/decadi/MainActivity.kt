package com.broulo.decadi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.clock.DigitalClock
import com.broulo.decadi.ui.theme.DecadiTheme
import com.broulo.decadi.ui.theme.LocalClockTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DecadiTheme {
                ClockScreen()
            }
        }
    }
}

@Composable
fun ClockScreen() {
    val theme = LocalClockTheme.current
    var time by remember { mutableStateOf(DecimalTime.now()) }

    // Refresh every ~864ms (1 decimal second) — seconds are hidden in V1
    LaunchedEffect(Unit) {
        while (true) {
            time = DecimalTime.now()
            delay(864L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background),
        contentAlignment = Alignment.Center
    ) {
        DigitalClock(time = time, showSeconds = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun ClockScreenPreview() {
    DecadiTheme {
        ClockScreen()
    }
}
