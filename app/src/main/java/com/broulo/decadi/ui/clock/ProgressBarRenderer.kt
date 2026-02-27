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

object ProgressBarRenderer {

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
        val paint = Paint().apply { isAntiAlias = true }

        val paddingH = width * 0.03f
        val paddingV = height * 0.22f
        val numberSize = height * 0.20f
        val numberGap = height * 0.04f
        val strokeWidth = height * 0.02f

        val barLeft = paddingH
        val barRight = width - paddingH
        val barWidth = barRight - barLeft
        val sectionWidth = barWidth / 10f

        // Numbers sit at the top: paddingV to paddingV + numberSize
        // Bar starts just below numbers + gap
        val barTop = paddingV + numberSize + numberGap
        // Bar bottom mirrors barTop distance from bottom
        val barBottom = height - barTop

        // Fill past sections + current section proportionally
        val progress = if (showSeconds) {
            time.hour + time.minute / 100f + time.second / 10000f
        } else {
            time.hour + time.minute / 100f
        }
        val fillWidth = (progress / 10f) * barWidth

        // Background fill of entire bar
        paint.color = bgColor
        paint.style = Paint.Style.FILL
        canvas.drawRect(barLeft, barTop, barRight, barBottom, paint)

        // Progress fill
        paint.color = primaryColor
        canvas.drawRect(barLeft, barTop, barLeft + fillWidth, barBottom, paint)

        // Separator lines between sections
        paint.color = secondaryColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth * 0.5f
        for (i in 1..9) {
            val x = barLeft + i * sectionWidth
            canvas.drawLine(x, barTop, x, barBottom, paint)
        }

        // Outline
        paint.strokeWidth = strokeWidth
        canvas.drawRect(barLeft, barTop, barRight, barBottom, paint)

        // Numbers 1-9 above separator lines
        paint.style = Paint.Style.FILL
        paint.textSize = numberSize
        paint.textAlign = Paint.Align.CENTER
        val textY = paddingV + numberSize
        for (i in 1..9) {
            paint.color = if (i == time.hour) primaryColor else secondaryColor
            val x = barLeft + i * sectionWidth
            canvas.drawText("$i", x, textY, paint)
        }

        // Minute count below the active section
        val minuteText = time.minute.toString().padStart(2, '0')
        paint.textSize = numberSize * 0.85f
        paint.color = primaryColor
        val activeSectionCenter = barLeft + time.hour * sectionWidth + sectionWidth / 2f
        val minuteY = barBottom + numberGap + paint.textSize
        canvas.drawText(minuteText, activeSectionCenter, minuteY, paint)
    }
}
