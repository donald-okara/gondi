/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun String.getInitials(): String {
    val words = this.trim().split("\\s+".toRegex())
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> (
            words[0].firstOrNull()?.toString().orEmpty() +
                words[1].firstOrNull()?.toString().orEmpty()
            ).uppercase()
    }
}

fun String.capitaliseFirst(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

@OptIn(ExperimentalTime::class)
fun Instant.toFormattedTime(): String {
    val now = Clock.System.now()
    val duration = now - this

    val minutes = duration.inWholeMinutes
    val hours = duration.inWholeHours
    val days = duration.inWholeDays

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
        hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
        // Cross-day handling
        days == 1L -> "Yesterday"
        else -> "$days days ago"
    }
}

fun String.formatArgs(vararg args: Any?): String {
    var formatted = this
    args.forEach { arg ->
        formatted = formatted.replaceFirst("%s", arg.toString())
    }
    return formatted
}