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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.theme.DecadiTheme
import com.broulo.decadi.ui.theme.LocalClockTheme

@Composable
fun DigitalClock(
    time: DecimalTime,
    showSeconds: Boolean = false,
    fontSize: TextUnit = 96.sp,
    modifier: Modifier = Modifier
) {
    val theme = LocalClockTheme.current
    val secondsFontSize = fontSize * 0.5f

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${time.hour}",
            modifier = Modifier.alignByBaseline(),
            color = theme.primary,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
        Text(
            text = ":",
            modifier = Modifier.alignByBaseline(),
            color = theme.secondary,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
        Text(
            text = time.minute.toString().padStart(2, '0'),
            modifier = Modifier.alignByBaseline(),
            color = theme.primary,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
        if (showSeconds) {
            Text(
                text = ":",
                modifier = Modifier.alignByBaseline(),
                color = theme.secondary,
                fontSize = secondsFontSize,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Default
            )
            Text(
                text = time.second.toString().padStart(2, '0'),
                modifier = Modifier.alignByBaseline(),
                color = theme.secondary,
                fontSize = secondsFontSize,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Default
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DigitalClockPreview() {
    DecadiTheme {
        DigitalClock(time = DecimalTime(5, 0, 0))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DigitalClockWithSecondsPreview() {
    DecadiTheme {
        DigitalClock(time = DecimalTime(7, 50, 42), showSeconds = true)
    }
}
