/*
 * Copyright (C) 2026 Théotime Dmitrašinović
 *
 * Licensed under the GNU General Public License v3.0
 * You may obtain a copy at https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.broulo.decadi.util

import com.broulo.decadi.model.DecimalTime

fun DecimalTime.formatWithoutSeconds(): String {
    return "$hour:${minute.toString().padStart(2, '0')}"
}

fun DecimalTime.formatWithSeconds(): String {
    return "$hour:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}"
}
