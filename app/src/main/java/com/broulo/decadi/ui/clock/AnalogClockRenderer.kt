/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.ui.clock

import android.graphics.Canvas
import android.graphics.Paint
import com.broulo.decadi.model.DecimalTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

object AnalogClockRenderer {

    fun draw(
        canvas: Canvas,
        width: Float,
        height: Float,
        time: DecimalTime,
        bgColor: Int,
        primaryColor: Int,
        secondaryColor: Int,
        accentColor: Int,
        showSeconds: Boolean = true
    ) {
        val size = min(width, height)
        val cx = width / 2f
        val cy = height / 2f
        val radius = size / 2f * 0.92f

        val paint = Paint().apply { isAntiAlias = true }

        // Background circle
        paint.color = bgColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)

        // Outer ring
        paint.color = primaryColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = size * 0.012f
        canvas.drawCircle(cx, cy, radius, paint)

        // Tick marks (100 minor, every 10th is major)
        for (i in 0 until 100) {
            val angleDeg = i * 3.6 - 90.0
            val angleRad = Math.toRadians(angleDeg)
            val isMajor = i % 10 == 0

            val outerR = radius * 0.95f
            val innerR = if (isMajor) radius * 0.80f else radius * 0.88f

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = if (isMajor) size * 0.015f else size * 0.004f
            paint.color = if (isMajor) primaryColor else secondaryColor

            val cosA = cos(angleRad).toFloat()
            val sinA = sin(angleRad).toFloat()
            canvas.drawLine(
                cx + cosA * innerR, cy + sinA * innerR,
                cx + cosA * outerR, cy + sinA * outerR,
                paint
            )
        }

        // Numbers 0-9
        paint.style = Paint.Style.FILL
        paint.color = secondaryColor
        paint.textSize = size * 0.085f
        paint.textAlign = Paint.Align.CENTER
        val fontMetrics = paint.fontMetrics
        val textCenterOffset = -(fontMetrics.ascent + fontMetrics.descent) / 2f
        for (i in 0 until 10) {
            val angleDeg = i * 36.0 - 90.0
            val angleRad = Math.toRadians(angleDeg)
            val numR = radius * 0.68f
            val x = cx + cos(angleRad).toFloat() * numR
            val y = cy + sin(angleRad).toFloat() * numR + textCenterOffset
            canvas.drawText("$i", x, y, paint)
        }

        // Hour hand
        val hourAngle = Math.toRadians((time.hour + time.minute / 100.0) * 36.0 - 90.0)
        paint.color = primaryColor
        paint.strokeWidth = size * 0.03f
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        canvas.drawLine(
            cx, cy,
            cx + cos(hourAngle).toFloat() * radius * 0.45f,
            cy + sin(hourAngle).toFloat() * radius * 0.45f,
            paint
        )

        // Minute hand
        val minAngle = Math.toRadians((time.minute + time.second / 100.0) * 3.6 - 90.0)
        paint.strokeWidth = size * 0.018f
        canvas.drawLine(
            cx, cy,
            cx + cos(minAngle).toFloat() * radius * 0.65f,
            cy + sin(minAngle).toFloat() * radius * 0.65f,
            paint
        )

        // Second hand
        if (showSeconds) {
            val secAngle = Math.toRadians(time.second * 3.6 - 90.0)
            paint.color = accentColor
            paint.strokeWidth = size * 0.006f
            canvas.drawLine(
                cx - cos(secAngle).toFloat() * radius * 0.15f,
                cy - sin(secAngle).toFloat() * radius * 0.15f,
                cx + cos(secAngle).toFloat() * radius * 0.78f,
                cy + sin(secAngle).toFloat() * radius * 0.78f,
                paint
            )

            // Center dot (accent)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(cx, cy, size * 0.022f, paint)
        } else {
            // Center dot (primary)
            paint.color = primaryColor
            paint.style = Paint.Style.FILL
            canvas.drawCircle(cx, cy, size * 0.018f, paint)
        }
    }
}
