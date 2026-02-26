package com.broulo.decadi.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.broulo.decadi.MainActivity
import com.broulo.decadi.model.DecimalTime

class ClockWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ClockWidgetContent()
        }
    }

    @Composable
    private fun ClockWidgetContent() {
        val context = LocalContext.current
        val time = DecimalTime.now()
        val primaryColor = ColorProvider(Color.White)
        val secondaryColor = ColorProvider(Color(0xFF888888))
        val bgColor = ColorProvider(Color.Black)
        val openAppIntent = Intent(context, MainActivity::class.java)

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(bgColor)
                .clickable(actionStartActivity(openAppIntent)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${time.hour}",
                    style = TextStyle(
                        color = primaryColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = ":",
                    style = TextStyle(
                        color = secondaryColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = time.minute.toString().padStart(2, '0'),
                    style = TextStyle(
                        color = primaryColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}
