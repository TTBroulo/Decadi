package com.broulo.decadi.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broulo.decadi.data.AppSettings
import com.broulo.decadi.data.ClockMode
import com.broulo.decadi.ui.theme.LocalClockTheme
import kotlin.math.roundToInt

private val PRESET_COLORS = listOf(
    Color(0xFF000000),
    Color(0xFFFFFFFF),
    Color(0xFFFF4444),
    Color(0xFF44FF44),
    Color(0xFF4488FF),
    Color(0xFFFFCC00),
    Color(0xFFFF8800),
    Color(0xFFCC44FF),
    Color(0xFF00CCCC),
    Color(0xFF888888),
    Color(0xFF444444),
    Color(0xFFCCCCCC),
)

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onSettingsChanged: (AppSettings) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalClockTheme.current
    val scrollState = rememberScrollState()

    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        // Header
        Text(
            text = "\u2190 Param\u00e8tres",
            color = theme.primary,
            fontSize = 20.sp,
            modifier = Modifier
                .clickable { onBack() }
                .padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(24.dp))

        // Clock mode
        SettingRow(label = "Mode d'affichage (inApp)") {
            Row {
                val isDigital = settings.clockMode == ClockMode.DIGITAL
                Text(
                    text = "Digital",
                    color = if (isDigital) theme.accent else theme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable { onSettingsChanged(settings.copy(clockMode = ClockMode.DIGITAL)) }
                        .padding(8.dp)
                )
                Text(
                    text = "Cadran",
                    color = if (!isDigital) theme.accent else theme.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable { onSettingsChanged(settings.copy(clockMode = ClockMode.ANALOG)) }
                        .padding(8.dp)
                )
            }
        }


        Spacer(Modifier.height(24.dp))

        // Font size
        SectionLabel("Taille de police (inApp) : ${settings.fontSizeSp} sp")
        Slider(
            value = settings.fontSizeSp.toFloat(),
            onValueChange = {
                onSettingsChanged(settings.copy(fontSizeSp = it.roundToInt()))
            },
            valueRange = 48f..144f,
            steps = 0,
            colors = sliderColors(),
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(24.dp))

        // Show seconds
        SettingRow(label = "Afficher les secondes") {
            Switch(
                checked = settings.showSeconds,
                onCheckedChange = { onSettingsChanged(settings.copy(showSeconds = it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = theme.accent,
                    checkedTrackColor = theme.accent.copy(alpha = 0.4f),
                    uncheckedThumbColor = theme.secondary,
                    uncheckedTrackColor = theme.secondary.copy(alpha = 0.2f)
                )
            )
        }


        Spacer(Modifier.height(24.dp))

        // Colors
        SectionLabel("Couleurs")
        Spacer(Modifier.height(12.dp))

        ColorSetting(
            label = "Fond",
            currentColor = settings.theme.background,
            showTransparent = true,
            onColorSelected = {
                onSettingsChanged(settings.copy(theme = settings.theme.copy(background = it)))
            }
        )
        Spacer(Modifier.height(16.dp))

        ColorSetting(
            label = "Principal",
            currentColor = settings.theme.primary,
            onColorSelected = {
                onSettingsChanged(settings.copy(theme = settings.theme.copy(primary = it)))
            }
        )
        Spacer(Modifier.height(16.dp))

        ColorSetting(
            label = "Secondaire",
            currentColor = settings.theme.secondary,
            onColorSelected = {
                onSettingsChanged(settings.copy(theme = settings.theme.copy(secondary = it)))
            }
        )
        Spacer(Modifier.height(16.dp))

        ColorSetting(
            label = "Accent",
            currentColor = settings.theme.accent,
            onColorSelected = {
                onSettingsChanged(settings.copy(theme = settings.theme.copy(accent = it)))
            }
        )

        Spacer(Modifier.height(48.dp))
    }
}

@Composable
private fun sliderColors() = SliderDefaults.colors(
    thumbColor = LocalClockTheme.current.accent,
    activeTrackColor = LocalClockTheme.current.accent,
    inactiveTrackColor = LocalClockTheme.current.secondary.copy(alpha = 0.3f)
)

@Composable
private fun SettingRow(
    label: String,
    content: @Composable () -> Unit
) {
    val theme = LocalClockTheme.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = theme.primary, fontSize = 16.sp)
        content()
    }
}

@Composable
private fun SectionLabel(text: String) {
    val theme = LocalClockTheme.current
    Text(text = text, color = theme.secondary, fontSize = 14.sp)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorSetting(
    label: String,
    currentColor: Color,
    showTransparent: Boolean = false,
    onColorSelected: (Color) -> Unit
) {
    val theme = LocalClockTheme.current
    var expanded by remember { mutableStateOf(false) }
    var hexInput by remember(currentColor) {
        mutableStateOf(String.format("%06X", currentColor.toArgb() and 0xFFFFFF))
    }
    val focusManager = LocalFocusManager.current

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(currentColor)
                    .border(1.dp, theme.secondary, CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Text(text = label, color = theme.primary, fontSize = 16.sp)
        }

        if (expanded) {
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showTransparent) {
                    val isTransparentSelected = currentColor.alpha == 0f
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .drawBehind {
                                val cellSize = size.width / 4f
                                for (row in 0 until 4) {
                                    for (col in 0 until 4) {
                                        drawRect(
                                            color = if ((row + col) % 2 == 0) Color.White else Color.LightGray,
                                            topLeft = Offset(col * cellSize, row * cellSize),
                                            size = Size(cellSize, cellSize)
                                        )
                                    }
                                }
                            }
                            .border(
                                width = if (isTransparentSelected) 3.dp else 1.dp,
                                color = if (isTransparentSelected) theme.accent else theme.secondary,
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(Color.Transparent) }
                    )
                }
                PRESET_COLORS.forEach { color ->
                    val isSelected = colorsEqual(color, currentColor)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) theme.accent else theme.secondary,
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(color) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "#", color = theme.secondary, fontSize = 16.sp)
                Spacer(Modifier.width(4.dp))
                OutlinedTextField(
                    value = hexInput,
                    onValueChange = { value ->
                        val filtered = value.filter { it.isLetterOrDigit() }.take(6).uppercase()
                        hexInput = filtered
                        if (filtered.length == 6) {
                            try {
                                val argb = android.graphics.Color.parseColor("#$filtered")
                                onColorSelected(Color(argb).copy(alpha = 1f))
                            } catch (_: Exception) { }
                        }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = theme.primary,
                        fontSize = 14.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.accent,
                        unfocusedBorderColor = theme.secondary,
                        cursorColor = theme.accent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}

private fun colorsEqual(a: Color, b: Color): Boolean {
    return (a.toArgb() and 0x00FFFFFF) == (b.toArgb() and 0x00FFFFFF)
}
