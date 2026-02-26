package com.broulo.decadi.util

import com.broulo.decadi.model.DecimalTime

fun DecimalTime.formatWithoutSeconds(): String {
    return "$hour:${minute.toString().padStart(2, '0')}"
}

fun DecimalTime.formatWithSeconds(): String {
    return "$hour:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}"
}
