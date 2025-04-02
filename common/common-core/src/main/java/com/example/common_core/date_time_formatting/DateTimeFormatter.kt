package com.example.common_core.date_time_formatting

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.formatDate(pattern: String = "MMM dd, yyyy"): String {
    val formatterForPattern = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return ZonedDateTime.parse(this, DateTimeFormatter.ISO_ZONED_DATE_TIME).format(formatterForPattern)
}