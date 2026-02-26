package com.broulo.decadi.ui.clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.broulo.decadi.model.DecimalTime
import com.broulo.decadi.ui.theme.DecadiTheme
import com.broulo.decadi.ui.theme.LocalClockTheme

@Composable
fun DigitalClock(
    time: DecimalTime,
    showSeconds: Boolean = false,
    modifier: Modifier = Modifier
) {
    val theme = LocalClockTheme.current
    val fontSize = 96.sp
    val secondsFontSize = 48.sp
    val separatorFontSize = 96.sp

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        // Hours
        Text(
            text = "${time.hour}",
            color = theme.primary,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
        // Separator
        Text(
            text = ":",
            color = theme.secondary,
            fontSize = separatorFontSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
        // Minutes (zero-padded to 2 digits)
        Text(
            text = time.minute.toString().padStart(2, '0'),
            color = theme.primary,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Default
        )
        if (showSeconds) {
            // Seconds separator
            Text(
                text = ":",
                color = theme.secondary,
                fontSize = secondsFontSize,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Default
            )
            // Seconds (zero-padded to 2 digits)
            Text(
                text = time.second.toString().padStart(2, '0'),
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
