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

/**
 * Generates initials from a full name string.
 *
 * This function processes a given name to produce a set of initials based on the following rules:
 * - If the name is empty, consists only of whitespace, or is otherwise invalid, an empty string is returned.
 * - If the name consists of a single word, the first two characters of that word are returned in uppercase.
 * - If the name consists of two or more words, the first character of the first word and the first character of the second word are concatenated and returned in uppercase.
 *
 * The function is robust against multiple spaces between words and leading/trailing whitespace.
 *
 * @param name The full name string to generate initials from.
 * @return The generated initials as a [String], following the rules described above.
 *
 * @sample
 * getInitials("Donald O. Isoe") // Returns "DO"
 * getInitials("Donald")         // Returns "DO"
 * getInitials("D")              // Returns "D"
 * getInitials("  ")             // Returns ""
 * getInitials("")               // Returns ""
 */
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

/**
 * Capitalizes the first letter of the string and converts the rest of the string to lowercase.
 * This is useful for formatting names or titles to a consistent sentence-case style.
 *
 * Example:
 * ```kotlin
 * "hELLo wORLd".capitaliseFirst() // Returns "Hello world"
 * "KOTLIN".capitaliseFirst()      // Returns "Kotlin"
 * "alreadyCapital".capitaliseFirst() // Returns "Alreadycapital"
 * ```
 *
 * @return A new string with the first character in uppercase and the remaining characters in lowercase.
 *         If the string is empty, it returns an empty string.
 */
fun String.capitaliseFirst(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

/**
 * Converts an [Instant] to a human-readable, relative time string.
 *
 * This function calculates the duration between the given [Instant] and the current time,
 * and formats it into a user-friendly string like "Just now", "5 minutes ago", "Yesterday", or "3 days ago".
 *
 * Examples:
 * - If the duration is less than a minute, it returns "Just now".
 * - If the duration is between 1 and 59 minutes, it returns "X minute(s) ago".
 * - If the duration is between 1 and 23 hours, it returns "X hour(s) ago".
 * - If the duration is exactly 1 day, it returns "Yesterday".
 * - For durations longer than one day, it returns "X days ago".
 *
 * @return A [String] representing the relative time elapsed since the [Instant].
 */
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

/**
 * Formats a string by replacing placeholders with the given arguments.
 *
 * This function sequentially replaces each occurrence of `%s` in the string
 * with the string representation of the corresponding argument in the `args` array.
 * It's a simple alternative to `String.format()` for basic substitution.
 *
 * Example:
 * ```kotlin
 * val template = "Hello, %s! Welcome to %s."
 * val formatted = template.formatArgs("World", "Kotlin")
 * // formatted will be "Hello, World! Welcome to Kotlin."
 * ```
 *
 * @param args The arguments to be substituted into the string.
 * @return A new string with the placeholders replaced by the arguments.
 */
fun String.formatArgs(vararg args: Any?): String {
    var formatted = this
    args.forEach { arg ->
        formatted = formatted.replaceFirst("%s", arg.toString())
    }
    return formatted
}
