/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.ui.clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.theme.LocalClockTheme

@Composable
fun FractionClock(
    time: DecimalTime,
    showSeconds: Boolean = false,
    fontSize: TextUnit = 96.sp,
    modifier: Modifier = Modifier
) {
    val theme = LocalClockTheme.current
    val prefixSize = fontSize * 0.5f

    // Compute fraction of day: totalDecimalSeconds / 100000
    val totalDecimalSeconds = time.hour * 10000 + time.minute * 100 + time.second
    val decimals = if (showSeconds) {
        String.format("%05d", totalDecimalSeconds)
    } else {
        String.format("%03d", totalDecimalSeconds / 100)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "0.",
            modifier = Modifier.alignByBaseline(),
            color = theme.secondary,
            fontSize = prefixSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
        Text(
            text = decimals,
            modifier = Modifier.alignByBaseline(),
            color = theme.primary,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
    }
}
