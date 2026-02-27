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
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.theme.LocalClockTheme

@Composable
fun ProgressBarClock(
    time: DecimalTime,
    showSeconds: Boolean = true,
    modifier: Modifier = Modifier
) {
    val theme = LocalClockTheme.current

    Canvas(modifier = modifier.aspectRatio(5f)) {
        drawIntoCanvas { canvas ->
            ProgressBarRenderer.draw(
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
