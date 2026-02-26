/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.ui.clock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.theme.DecadiTheme
import com.broulo.decadi.ui.theme.LocalClockTheme

@Composable
fun AnalogClock(
    time: DecimalTime,
    showSeconds: Boolean = true,
    modifier: Modifier = Modifier
) {
    val theme = LocalClockTheme.current

    Canvas(modifier = modifier.aspectRatio(1f)) {
        drawIntoCanvas { canvas ->
            AnalogClockRenderer.draw(
                canvas = canvas.nativeCanvas,
                width = size.width,
                height = size.height,
                time = time,
                bgColor = theme.background.toArgb(),
                primaryColor = theme.primary.toArgb(),
                secondaryColor = theme.secondary.toArgb(),
                accentColor = theme.accent.toArgb(),
                showSeconds = showSeconds
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AnalogClockPreview() {
    DecadiTheme {
        AnalogClock(time = DecimalTime(5, 0, 0))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AnalogClockWithTimePreview() {
    DecadiTheme {
        AnalogClock(time = DecimalTime(7, 50, 42))
    }
}
