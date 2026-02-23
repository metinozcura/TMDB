package com.metinozcura.tmdb.common.util

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@SuppressLint("DefaultLocale")
fun Long.formatMoney(): String {
    return if (this >= 1_000_000) {
        String.format("$%.1fM", this / 1_000_000.0)
    } else if (this >= 1_000) {
        String.format("$%.1fK", this / 1_000.0)
    } else {
        "$$this"
    }
}

fun String.formatReleaseDate(): String {
    if (this.isBlank()) return ""
    return try {
        val date = LocalDate.parse(this)
        date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    } catch (_: DateTimeParseException) {
        this
    }
}

fun Int.formatRuntime(): String {
    val h = this / 60
    val m = this % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}
