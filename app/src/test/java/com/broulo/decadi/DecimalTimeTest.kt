/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi

import com.broulo.decadi.model.DecimalTime
import org.junit.Assert.assertEquals
import org.junit.Test

class DecimalTimeTest {

    @Test
    fun midnight_is_0_00_00() {
        val dt = DecimalTime.fromStandard(0, 0, 0)
        assertEquals(0, dt.hour)
        assertEquals(0, dt.minute)
        assertEquals(0, dt.second)
    }

    @Test
    fun six_am_is_2_50_00() {
        val dt = DecimalTime.fromStandard(6, 0, 0)
        assertEquals(2, dt.hour)
        assertEquals(50, dt.minute)
        assertEquals(0, dt.second)
    }

    @Test
    fun noon_is_5_00_00() {
        val dt = DecimalTime.fromStandard(12, 0, 0)
        assertEquals(5, dt.hour)
        assertEquals(0, dt.minute)
        assertEquals(0, dt.second)
    }

    @Test
    fun six_pm_is_7_50_00() {
        val dt = DecimalTime.fromStandard(18, 0, 0)
        assertEquals(7, dt.hour)
        assertEquals(50, dt.minute)
        assertEquals(0, dt.second)
    }

    @Test
    fun end_of_day_is_9_99_98() {
        // 23:59:59 = 86399s → 99998.84 decimal seconds → truncates to 9:99:98
        // 9:99:99 requires 23:59:59.136+
        val dt = DecimalTime.fromStandard(23, 59, 59)
        assertEquals(9, dt.hour)
        assertEquals(99, dt.minute)
        assertEquals(98, dt.second)
    }

    @Test
    fun end_of_day_with_millis_is_9_99_99() {
        val dt = DecimalTime.fromStandard(23, 59, 59, 999)
        assertEquals(9, dt.hour)
        assertEquals(99, dt.minute)
        assertEquals(99, dt.second)
    }
}
