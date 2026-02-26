/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.model

import java.util.Calendar

data class DecimalTime(
    val hour: Int,
    val minute: Int,
    val second: Int
) {
    companion object {
        fun now(): DecimalTime {
            val cal = Calendar.getInstance()
            val h = cal.get(Calendar.HOUR_OF_DAY)
            val m = cal.get(Calendar.MINUTE)
            val s = cal.get(Calendar.SECOND)
            val ms = cal.get(Calendar.MILLISECOND)
            return fromStandard(h, m, s, ms)
        }

        fun fromStandard(hours: Int, minutes: Int, seconds: Int, millis: Int = 0): DecimalTime {
            val totalStandardSeconds = hours * 3600.0 + minutes * 60.0 + seconds + millis / 1000.0
            val fractionOfDay = totalStandardSeconds / 86400.0
            val totalDecimalSeconds = (fractionOfDay * 100_000).toInt()

            val decHour = totalDecimalSeconds / 10_000
            val remainder = totalDecimalSeconds % 10_000
            val decMinute = remainder / 100
            val decSecond = remainder % 100

            return DecimalTime(decHour, decMinute, decSecond)
        }
    }
}
